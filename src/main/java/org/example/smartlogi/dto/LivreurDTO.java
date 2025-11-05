package org.example.smartlogi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LivreurDTO {

    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Size(min = 2, max = 100, message = "Le prénom doit contenir entre 2 et 100 caractères")
    private String prenom;

    @NotBlank(message = "Le téléphone est obligatoire")
    @Size(min = 10, max = 20, message = "Le téléphone doit contenir entre 10 et 20 caractères")
    private String telephone;

    @NotBlank(message = "Le type de véhicule est obligatoire")
    @Size(max = 50, message = "Le type de véhicule ne doit pas dépasser 50 caractères")
    private String vehicule;

    @NotNull(message = "La zone assignée est obligatoire")
    private Long zoneAssigneeId;

    private String zoneNom;
    private String zoneVille;

    private Boolean actif = true;

    private LocalDateTime createdAt;
}