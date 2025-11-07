package org.example.smartlogi.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ColisDTO {

    private Long id;

    @NotBlank(message = "Le numéro de suivi est obligatoire")
    @Size(max = 50, message = "Le numéro de suivi ne doit pas dépasser 50 caractères")
    private String numeroSuivi;

    @NotNull(message = "Le client expéditeur est obligatoire")
    private Long clientExpediteurId;

    @NotNull(message = "Le destinataire est obligatoire")
    private Long destinataireId;

    @NotNull(message = "La zone de destination est obligatoire")
    private Long zoneDestinationId;

    private Long livreurAssigneId;

    @NotBlank(message = "Le statut est obligatoire")
    private String statut;

    @NotBlank(message = "La priorité est obligatoire")
    private String priorite;

    @NotNull(message = "Le poids total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids total doit être supérieur à 0")
    private BigDecimal poidsTotal;

    @NotNull(message = "Le prix total est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix total doit être supérieur à 0")
    private BigDecimal prixTotal;

    private String adresseLivraison;

    private String commentaire;

    private LocalDateTime dateLivraisonPrevue;

    private LocalDateTime dateLivraisonReelle;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String clientExpediteurNom;
    private String destinataireNom;
    private String zoneDestinationNom;
    private String livreurAssigneNom;
}