package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.ProduitDTO;
import org.example.smartlogi.service.ProduitService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/produits")
@RequiredArgsConstructor
@Tag(name = "Produits", description = "API de gestion des produits")
public class ProduitController {

    private final ProduitService produitService;

    @PostMapping
    @Operation(summary = "Créer un produit", description = "Créer un nouveau produit")
    public ResponseEntity<ProduitDTO> create(@Valid @RequestBody ProduitDTO dto) {
        ProduitDTO created = produitService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un produit", description = "Récupérer un produit par son ID")
    public ResponseEntity<ProduitDTO> findById(@PathVariable Long id) {
        ProduitDTO dto = produitService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Lister tous les produits", description = "Récupérer la liste de tous les produits")
    public ResponseEntity<List<ProduitDTO>> findAll() {
        List<ProduitDTO> produits = produitService.findAll();
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/page")
    @Operation(summary = "Lister les produits (paginé)", description = "Récupérer les produits avec pagination")
    public ResponseEntity<Page<ProduitDTO>> findAllPaginated(Pageable pageable) {
        Page<ProduitDTO> page = produitService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un produit", description = "Mettre à jour les informations d'un produit")
    public ResponseEntity<ProduitDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ProduitDTO dto) {
        ProduitDTO updated = produitService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un produit", description = "Supprimer un produit par son ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produitService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/categorie/{categorie}")
    @Operation(summary = "Rechercher par catégorie", description = "Trouver tous les produits d'une catégorie")
    public ResponseEntity<List<ProduitDTO>> findByCategorie(@PathVariable String categorie) {
        List<ProduitDTO> produits = produitService.findByCategorie(categorie);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher par nom", description = "Rechercher des produits par nom (recherche partielle)")
    public ResponseEntity<List<ProduitDTO>> searchByNom(@RequestParam String nom) {
        List<ProduitDTO> produits = produitService.searchByNom(nom);
        return ResponseEntity.ok(produits);
    }

    @GetMapping("/categorie/{categorie}/trier-par-prix")
    @Operation(summary = "Produits par catégorie triés", description = "Récupérer les produits d'une catégorie triés par prix croissant")
    public ResponseEntity<List<ProduitDTO>> findByCategorieOrderByPrix(@PathVariable String categorie) {
        List<ProduitDTO> produits = produitService.findByCategorieOrderByPrix(categorie);
        return ResponseEntity.ok(produits);
    }
}