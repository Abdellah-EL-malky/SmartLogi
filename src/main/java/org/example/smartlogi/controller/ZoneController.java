package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.ZoneDTO;
import org.example.smartlogi.service.ZoneService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@RequiredArgsConstructor
@Tag(name = "Zones", description = "API de gestion des zones")
public class ZoneController {

    private final ZoneService zoneService;

    @GetMapping
    @PreAuthorize("hasAuthority('ZONE_READ')")
    @Operation(summary = "Lister toutes les zones")
    public ResponseEntity<List<ZoneDTO>> findAll() {
        List<ZoneDTO> zones = zoneService.findAll();
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Récupérer une zone par ID")
    public ResponseEntity<ZoneDTO> findById(@PathVariable Long id) {
        ZoneDTO dto = zoneService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Créer une nouvelle zone")
    public ResponseEntity<ZoneDTO> create(@Valid @RequestBody ZoneDTO zoneDTO) {
        ZoneDTO created = zoneService.create(zoneDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Modifier une zone")
    public ResponseEntity<ZoneDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ZoneDTO zoneDTO) {
        ZoneDTO updated = zoneService.update(id, zoneDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Supprimer une zone")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        zoneService.delete(id);
        return ResponseEntity.noContent().build();
    }
}