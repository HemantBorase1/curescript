package com.curescript.entity;

import jakarta.persistence.*;

@Entity
@Table(name="remedy_suggestions")
public class RemedySuggestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String remedyText;
    private String source;
    @OneToOne(fetch =FetchType.LAZY)
    @JoinColumn(name="medicine_id" , nullable = false,unique = true)
    private Medicine medicine;



    public RemedySuggestion(){
    }

    public RemedySuggestion(Long id, String remedyText, String source, Medicine medicine) {
        this.id = id;
        this.remedyText = remedyText;
        this.source = source;
        this.medicine = medicine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemedyText() {
        return remedyText;
    }

    public void setRemedyText(String remedyText) {
        this.remedyText = remedyText;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public void setMedicine(Medicine medicine) {
        this.medicine = medicine;
    }
}
