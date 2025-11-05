package org.example.smartlogi.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {

    private Long id;

    @NotBlank(message = "Le nom de la zone est obligatoire")
    @Size(min = 2, max = 100, message = "Le nom doit contenir entre 2 et 100 caractères")
    private String nom;

    @NotBlank(message = "Le code postal est obligatoire")
    @Size(min = 4, max = 10, message = "Le code postal doit contenir entre 4 et 10 caractères")
    private String codePostal;

    @NotBlank(message = "La ville est obligatoire")
    @Size(min = 2, max = 100, message = "La ville doit contenir entre 2 et 100 caractères")
    private String ville;

    private LocalDateTime createdAt;
}