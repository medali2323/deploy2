package com.example.pfe.models;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;

@Entity
public class Categ_Instructeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    private String description;
   @OneToMany(mappedBy="categInstructeur",fetch=FetchType.LAZY)
    private List<Instructor> instructors;
   @OneToOne
   	private Pourcentage Pourcentage;
    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getdescription() {
        return description;
    }

    public void setdescription(String description) {
        this.description = description;
    }

	public Pourcentage getPourcentage() {
		return Pourcentage;
	}

	public void setPourcentage(Pourcentage pourcentage) {
		Pourcentage = pourcentage;
	}
    
}
