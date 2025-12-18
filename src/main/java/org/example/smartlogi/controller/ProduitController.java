package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.ProduitDTO;
import org.example.smartlogi.service.ProduitService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "API de gestion des produits")
public class ProduitController {

    private final ProduitService produitService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @Operation(summary = "Lister tous les produits")
    public ResponseEntity<List<ProduitDTO>> findAll() {
        List<ProduitDTO> produits = produitService.findAll();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @Operation(summary = "Récupérer un produit par ID")
    public ResponseEntity<ProduitDTO> findById(@PathVariable Long id) {
        ProduitDTO dto = produitService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Créer un nouveau produit")
    public ResponseEntity<ProduitDTO> create(@Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO created = produitService.create(produitDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Modifier un produit")
    public ResponseEntity<ProduitDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProduitDTO produitDTO) {
        ProduitDTO updated = produitService.update(id, produitDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Supprimer un produit")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }
}