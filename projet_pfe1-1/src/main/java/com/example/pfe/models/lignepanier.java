package com.example.pfe.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class lignepanier {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int qte;
  
    @ManyToOne
    @JoinColumn(name = "produit_id") // This references the Produit entity
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "panier_id") // This references the Vente_prod entity
    private panier panier;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getQte() {
		return qte;
	}

	public void setQte(int qte) {
		this.qte = qte;
	}

	public Produit getProduit() {
		return produit;
	}

	public void setProduit(Produit produit) {
		this.produit = produit;
	}

	public panier getPanier() {
		return panier;
	}

	public void setPanier(panier panier) {
		this.panier = panier;
	}
    
}