package org.example.smartlogi.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateColisRequest {

    @NotNull(message = "Le client expéditeur est obligatoire")
    private Long clientExpediteurId;

    @NotNull(message = "Le destinataire est obligatoire")
    private Long destinataireId;

    @NotNull(message = "La zone de destination est obligatoire")
    private Long zoneDestinationId;

    @NotBlank(message = "La priorité est obligatoire")
    private String priorite;

    private String adresseLivraison;

    private String commentaire;

    private LocalDateTime dateLivraisonPrevue;

    @NotEmpty(message = "La liste de produits ne peut pas être vide")
    @Valid
    private List<CreateColisProduitRequest> produits;
}