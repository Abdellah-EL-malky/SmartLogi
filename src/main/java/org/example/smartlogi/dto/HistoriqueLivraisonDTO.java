package org.example.smartlogi.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HistoriqueLivraisonDTO {

    private Long id;

    private Long colisId;

    @NotBlank(message = "Le statut est obligatoire")
    private String statut;

    private String commentaire;

    private String localisation;

    private LocalDateTime dateChangement;

    private String livreurNom;
}