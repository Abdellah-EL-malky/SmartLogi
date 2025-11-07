package org.example.smartlogi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisDetailDTO {

    private Long id;
    private String numeroSuivi;

    private Long clientExpediteurId;
    private String clientExpediteurNom;
    private String clientExpediteurPrenom;
    private String clientExpediteurEmail;
    private String clientExpediteurTelephone;

    private Long destinataireId;
    private String destinataireNom;
    private String destinatairePrenom;
    private String destinataireEmail;
    private String destinataireTelephone;
    private String destinataireAdresse;

    private Long zoneDestinationId;
    private String zoneDestinationNom;
    private String zoneDestinationVille;
    private String zoneDestinationCodePostal;

    private Long livreurAssigneId;
    private String livreurAssigneNom;
    private String livreurAssignePrenom;
    private String livreurAssigneTelephone;
    private String livreurAssigneVehicule;

    private String statut;
    private String priorite;
    private BigDecimal poidsTotal;
    private BigDecimal prixTotal;
    private String adresseLivraison;
    private String commentaire;
    private LocalDateTime dateLivraisonPrevue;
    private LocalDateTime dateLivraisonReelle;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ColisProduitDTO> produits = new ArrayList<>();

    private List<HistoriqueLivraisonDTO> historique = new ArrayList<>();
}