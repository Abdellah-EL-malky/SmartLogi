package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.LivreurDTO;
import org.example.smartlogi.service.LivreurService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
@Tag(name = "Livreurs", description = "API de gestion des livreurs")
public class LivreurController {

    private final LivreurService livreurService;

    @PostMapping
    @Operation(summary = "Créer un livreur", description = "Créer un nouveau livreur")
    public ResponseEntity<LivreurDTO> create(@Valid @RequestBody LivreurDTO dto) {
        LivreurDTO created = livreurService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un livreur", description = "Récupérer un livreur par son ID")
    public ResponseEntity<LivreurDTO> findById(@PathVariable Long id) {
        LivreurDTO dto = livreurService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Lister tous les livreurs", description = "Récupérer la liste de tous les livreurs")
    public ResponseEntity<List<LivreurDTO>> findAll() {
        List<LivreurDTO> livreurs = livreurService.findAll();
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/page")
    @Operation(summary = "Lister les livreurs (paginé)", description = "Récupérer les livreurs avec pagination")
    public ResponseEntity<Page<LivreurDTO>> findAllPaginated(Pageable pageable) {
        Page<LivreurDTO> page = livreurService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un livreur", description = "Mettre à jour les informations d'un livreur")
    public ResponseEntity<LivreurDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LivreurDTO dto) {
        LivreurDTO updated = livreurService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un livreur", description = "Supprimer un livreur par son ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livreurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/telephone/{telephone}")
    @Operation(summary = "Rechercher par téléphone", description = "Trouver un livreur par son téléphone")
    public ResponseEntity<LivreurDTO> findByTelephone(@PathVariable String telephone) {
        LivreurDTO dto = livreurService.findByTelephone(telephone);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "Livreurs par zone", description = "Récupérer tous les livreurs d'une zone")
    public ResponseEntity<List<LivreurDTO>> findByZone(@PathVariable Long zoneId) {
        List<LivreurDTO> livreurs = livreurService.findByZone(zoneId);
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/actifs")
    @Operation(summary = "Livreurs actifs", description = "Récupérer tous les livreurs actifs")
    public ResponseEntity<List<LivreurDTO>> findActifs() {
        List<LivreurDTO> livreurs = livreurService.findActifs();
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/actifs/zone/{zoneId}")
    @Operation(summary = "Livreurs actifs par zone", description = "Récupérer les livreurs actifs d'une zone")
    public ResponseEntity<List<LivreurDTO>> findActifsByZone(@PathVariable Long zoneId) {
        List<LivreurDTO> livreurs = livreurService.findActifsByZone(zoneId);
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher par nom", description = "Rechercher des livreurs par nom (recherche partielle)")
    public ResponseEntity<List<LivreurDTO>> searchByNom(@RequestParam String nom) {
        List<LivreurDTO> livreurs = livreurService.searchByNom(nom);
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/vehicule/{vehicule}")
    @Operation(summary = "Rechercher par véhicule", description = "Trouver les livreurs par type de véhicule")
    public ResponseEntity<List<LivreurDTO>> findByVehicule(@PathVariable String vehicule) {
        List<LivreurDTO> livreurs = livreurService.findByVehicule(vehicule);
        return ResponseEntity.ok(livreurs);
    }

    @PatchMapping("/{id}/activer")
    @Operation(summary = "Activer un livreur", description = "Activer un livreur (le rendre disponible)")
    public ResponseEntity<LivreurDTO> activer(@PathVariable Long id) {
        LivreurDTO dto = livreurService.setActif(id, true);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{id}/desactiver")
    @Operation(summary = "Désactiver un livreur", description = "Désactiver un livreur (le rendre indisponible)")
    public ResponseEntity<LivreurDTO> desactiver(@PathVariable Long id) {
        LivreurDTO dto = livreurService.setActif(id, false);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/count/actifs/zone/{zoneId}")
    @Operation(summary = "Compter livreurs actifs", description = "Compter le nombre de livreurs actifs d'une zone")
    public ResponseEntity<Long> countActifsByZone(@PathVariable Long zoneId) {
        Long count = livreurService.countActifsByZone(zoneId);
        return ResponseEntity.ok(count);
    }
}