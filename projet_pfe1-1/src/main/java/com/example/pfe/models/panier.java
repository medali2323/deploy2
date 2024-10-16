package com.example.pfe.models;

import java.util.Date;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class panier {
	  @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    private String code;
	    private Date date;
	    private double totHt;
	    private double totTtc;
	    @ManyToOne
	    @JoinColumn(name = "instructor_id")
	    private Instructor instructor;
	    @OneToMany(mappedBy = "panier")
	    private List<lignepanier> lignepanier;
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
		public Date getDate() {
			return date;
		}
		public void setDate(Date date) {
			this.date = date;
		}
		public double getTotHt() {
			return totHt;
		}
		public void setTotHt(double totHt) {
			this.totHt = totHt;
		}
		public double getTotTtc() {
			return totTtc;
		}
		public void setTotTtc(double totTtc) {
			this.totTtc = totTtc;
		}
		public Instructor getInstructor() {
			return instructor;
		}
		public void setInstructor(Instructor instructor) {
			this.instructor = instructor;
		}

}
