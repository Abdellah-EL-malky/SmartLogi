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
public class ProduitDTO {

    private Long id;

    @NotBlank(message = "Le nom du produit est obligatoire")
    @Size(min = 2, max = 200, message = "Le nom doit contenir entre 2 et 200 caractères")
    private String nom;

    @Size(max = 100, message = "La catégorie ne doit pas dépasser 100 caractères")
    private String categorie;

    @NotNull(message = "Le poids est obligatoire")
    @DecimalMin(value = "0.01", message = "Le poids doit être supérieur à 0")
    private BigDecimal poids;

    @NotNull(message = "Le prix est obligatoire")
    @DecimalMin(value = "0.01", message = "Le prix doit être supérieur à 0")
    private BigDecimal prix;

    private LocalDateTime createdAt;
}