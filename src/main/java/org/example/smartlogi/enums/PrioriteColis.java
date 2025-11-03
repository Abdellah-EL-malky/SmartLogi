package org.example.smartlogi.enums;

public enum PrioriteColis {
    NORMALE("Normale"),
    URGENTE("Urgente"),
    TRES_URGENTE("Très urgente");

    private final String libelle;

    PrioriteColis(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}