package org.example.smartlogi.enums;

public enum StatutColis {
    CREE("Créé"),
    EN_PREPARATION("En préparation"),
    COLLECTE("Collecté"),
    EN_STOCK("En stock"),
    EN_TRANSIT("En transit"),
    LIVRE("Livré");

    private final String libelle;

    StatutColis(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}