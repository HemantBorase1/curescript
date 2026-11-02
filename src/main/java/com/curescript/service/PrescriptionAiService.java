
package com.curescript.service;

import com.curescript.dto.PrescriptionAiResponseDTO;
import com.curescript.entity.Medicine;
import com.curescript.entity.Prescription;
import com.curescript.repository.MedicineRepository;
import com.curescript.repository.PrescriptionRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class PrescriptionAiService {

    private final PrescriptionRepository prescriptionRepository;
    private final ObjectMapper objectMapper;
    private final PrescriptionAiResponseValidator responseValidator;
    private final MedicineRepository medicineRepository;

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.model}")
    private String model;

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(20))
            .build();

    public PrescriptionAiService(
            PrescriptionRepository prescriptionRepository,
            ObjectMapper objectMapper, PrescriptionAiResponseValidator responseValidator, MedicineRepository medicineRepository) {
        this.prescriptionRepository = prescriptionRepository;
        this.objectMapper = objectMapper;
        this.responseValidator = responseValidator;
        this.medicineRepository = medicineRepository;
    }

    public PrescriptionAiResponseDTO extractPrescriptionInfo(
            Long prescriptionId) throws Exception {

        // 1. Find prescription in MySQL
        Prescription prescription = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Prescription not found"));

        // 2. Verify ownership
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !prescription.getPatient().getEmail()
                .equalsIgnoreCase(authentication.getName())) {
            throw new AccessDeniedException(
                    "You cannot access this prescription");
        }

        // 3. Validate the stored Cloudinary URL
        URI imageUri = URI.create(prescription.getRawImageUrl());

        if (!"https".equalsIgnoreCase(imageUri.getScheme())
                || !"res.cloudinary.com".equalsIgnoreCase(
                imageUri.getHost())) {
            throw new IllegalArgumentException(
                    "Invalid prescription image URL");
        }

        // 4. Download image from Cloudinary
        HttpRequest imageRequest = HttpRequest.newBuilder(imageUri)
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();

        HttpResponse<byte[]> imageResponse = httpClient.send(
                imageRequest,
                HttpResponse.BodyHandlers.ofByteArray());

        if (imageResponse.statusCode() != 200) {
            throw new IllegalStateException(
                    "Could not download prescription image");
        }

        byte[] imageBytes = imageResponse.body();

        if (imageBytes.length > 10 * 1024 * 1024) {
            throw new IllegalArgumentException(
                    "Prescription image must be under 10 MB");
        }

        String contentType = imageResponse.headers()
                .firstValue("Content-Type")
                .orElse("");

        if (!contentType.toLowerCase().startsWith("image/")) {
            throw new IllegalArgumentException(
                    "Cloudinary did not return a valid image type");
        }

        // 5. Convert image bytes to Base64
        String base64Image = Base64.getEncoder()
                .encodeToString(imageBytes);

        // 6. Prepare prompt for Gemini
        String prompt = """
                You are a prescription text extraction assistant.

                Carefully inspect the entire prescription image,
                including handwritten text, printed text, medicine
                names, and dosage details.

                Return valid JSON using this exact structure:
                {
                  "doctorName": null,
                  "medicines": [
                    {
                      "name": null,
                      "dosage": null,
                      "frequency": null,
                      "duration": null,
                      "instructions": null,
                      "uncertainFields": []
                    }
                  ],
                  "needsProfessionalVerification": true
                }

                Rules:
                1. Do not invent medicine names, dosages, or instructions.
                2. Keep dosage, frequency, duration, and route separate.
                3. Use null for unreadable or missing information.
                4. Include uncertain field names in uncertainFields.
                5. Do not confuse IV, Inj, Tab, or IM with medicine names.
                6. Include only medicines actually indicated in the image.
                7. Set needsProfessionalVerification to true if any
                   medicine name, dosage, or instruction is uncertain.
                8. Return JSON only, without Markdown formatting.
                """;

        Map<String, Object> imagePart = Map.of(
                "inline_data",
                Map.of(
                        "mime_type", contentType,
                        "data", base64Image
                )
        );

        Map<String, Object> textPart = Map.of(
                "text", prompt
        );

        Map<String, Object> requestBody = new LinkedHashMap<>();

        requestBody.put("contents", List.of(
                Map.of("parts", List.of(textPart, imagePart))
        ));

        requestBody.put("generationConfig", Map.of(
                "response_mime_type", "application/json",
                "temperature", 0
        ));

        // 7. Send image and prompt to Gemini
        String endpoint =
                "https://generativelanguage.googleapis.com/v1beta/models/"
                        + model + ":generateContent";

        HttpRequest aiRequest = HttpRequest.newBuilder()
                .uri(URI.create(endpoint))
                .timeout(Duration.ofSeconds(60))
                .header("Content-Type", "application/json")
                .header("x-goog-api-key", apiKey)
                .POST(HttpRequest.BodyPublishers.ofString(
                        objectMapper.writeValueAsString(requestBody)))
                .build();

        HttpResponse<String> aiResponse = httpClient.send(
                aiRequest,
                HttpResponse.BodyHandlers.ofString());

        if (aiResponse.statusCode() != 200) {
            throw new IllegalStateException(
                    "Gemini API request failed. Status: "
                            + aiResponse.statusCode()
                            + ", Details: " + aiResponse.body());
        }

        // 8. Extract Gemini's response text
        JsonNode responseJson =
                objectMapper.readTree(aiResponse.body());

        JsonNode result = responseJson
                .path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text");

        if (result.isMissingNode() || result.isNull()
                || result.asText().isBlank()) {
            throw new IllegalStateException(
                    "Gemini returned no prescription information");
        }

        // 9. Convert JSON text into the Java DTO
        PrescriptionAiResponseDTO response = objectMapper.readValue(
                result.asText(),
                PrescriptionAiResponseDTO.class);

        return responseValidator.validate(response);
    }


    @Transactional
    public PrescriptionAiResponseDTO extractAndSaveMedicines(
            Long prescriptionId) throws Exception {

        // 1. Find prescription in MySQL
        Prescription prescription = prescriptionRepository
                .findById(prescriptionId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Prescription not found"));

        // 2. Verify ownership before checking or processing medicines
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !prescription.getPatient().getEmail()
                .equalsIgnoreCase(authentication.getName())) {
            throw new AccessDeniedException(
                    "You cannot access this prescription");
        }

        // 3. Prevent duplicate extraction and saving
        if (!medicineRepository
                .findByPrescription_Id(prescriptionId)
                .isEmpty()) {
            throw new IllegalStateException(
                    "Medicines have already been extracted for this prescription");
        }

        // 4. Extract information using Gemini
        PrescriptionAiResponseDTO response =
                extractPrescriptionInfo(prescriptionId);

        if (response.getMedicines() == null
                || response.getMedicines().isEmpty()) {
            throw new IllegalStateException(
                    "No medicines were extracted from the prescription");
        }

        // 5. Save extracted medicines
        int savedCount = 0;

        for (var extractedMedicine : response.getMedicines()) {

            // Skip records without a medicine name
            if (extractedMedicine.getName() == null
                    || extractedMedicine.getName().isBlank()) {
                continue;
            }

            Medicine medicine = new Medicine();

            medicine.setName(extractedMedicine.getName());
            medicine.setDosage(extractedMedicine.getDosage());
            medicine.setFrequency(extractedMedicine.getFrequency());
            medicine.setDuration(extractedMedicine.getDuration());

            // Preserve uncertainty as JSON text
            medicine.setUncertainFields(
                    objectMapper.writeValueAsString(
                            extractedMedicine.getUncertainFields()));

            // AI results remain unverified
            medicine.setVerified(false);

            // Associate medicine with the correct prescription
            medicine.setPrescription(prescription);

            medicineRepository.save(medicine);
            savedCount++;
        }

        if (savedCount == 0) {
            throw new IllegalStateException(
                    "No medicines with readable names could be saved");
        }

        return response;
    }


}
