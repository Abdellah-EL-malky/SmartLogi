package org.example.smartlogi.enums;

public enum TypeVehicule {
    VOITURE("Voiture"),
    CAMIONNETTE("Camionnette"),
    MOTO("Moto");

    private final String libelle;

    TypeVehicule(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}