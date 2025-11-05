package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.ZoneDTO;
import org.example.smartlogi.service.ZoneService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@Tag(name = "Zones", description = "API de gestion des zones de livraison")
public class ZoneController {

    private final ZoneService zoneService;

    @PostMapping
    @Operation(summary = "Créer une zone", description = "Créer une nouvelle zone de livraison")
    public ResponseEntity<ZoneDTO> create(@Valid @RequestBody ZoneDTO dto) {
        ZoneDTO created = zoneService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une zone", description = "Récupérer une zone par son ID")
    public ResponseEntity<ZoneDTO> findById(@PathVariable Long id) {
        ZoneDTO dto = zoneService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Lister toutes les zones", description = "Récupérer la liste de toutes les zones")
    public ResponseEntity<List<ZoneDTO>> findAll() {
        List<ZoneDTO> zones = zoneService.findAll();
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/page")
    @Operation(summary = "Lister les zones (paginé)", description = "Récupérer les zones avec pagination")
    public ResponseEntity<Page<ZoneDTO>> findAllPaginated(Pageable pageable) {
        Page<ZoneDTO> page = zoneService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier une zone", description = "Mettre à jour les informations d'une zone")
    public ResponseEntity<ZoneDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ZoneDTO dto) {
        ZoneDTO updated = zoneService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une zone", description = "Supprimer une zone par son ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/nom/{nom}")
    @Operation(summary = "Rechercher par nom", description = "Trouver une zone par son nom")
    public ResponseEntity<ZoneDTO> findByNom(@PathVariable String nom) {
        ZoneDTO dto = zoneService.findByNom(nom);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/ville/{ville}")
    @Operation(summary = "Rechercher par ville", description = "Trouver toutes les zones d'une ville")
    public ResponseEntity<List<ZoneDTO>> findByVille(@PathVariable String ville) {
        List<ZoneDTO> zones = zoneService.findByVille(ville);
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/code-postal/{codePostal}")
    @Operation(summary = "Rechercher par code postal", description = "Trouver une zone par son code postal")
    public ResponseEntity<ZoneDTO> findByCodePostal(@PathVariable String codePostal) {
        ZoneDTO dto = zoneService.findByCodePostal(codePostal);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/exists/nom/{nom}")
    @Operation(summary = "Vérifier l'existence d'un nom", description = "Vérifier si un nom de zone existe déjà")
    public ResponseEntity<Boolean> existsByNom(@PathVariable String nom) {
        boolean exists = zoneService.existsByNom(nom);
        return ResponseEntity.ok(exists);
    }
}