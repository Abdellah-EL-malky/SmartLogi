package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.LivreurDTO;
import org.example.smartlogi.service.LivreurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/livreurs")
@RequiredArgsConstructor
@Tag(name = "Livreurs", description = "API de gestion des livreurs")
public class LivreurController {

    private final LivreurService livreurService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Lister tous les livreurs")
    public ResponseEntity<List<LivreurDTO>> findAll() {
        List<LivreurDTO> livreurs = livreurService.findAll();
        return ResponseEntity.ok(livreurs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Récupérer un livreur par ID")
    public ResponseEntity<LivreurDTO> findById(@PathVariable Long id) {
        LivreurDTO dto = livreurService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Créer un nouveau livreur")
    public ResponseEntity<LivreurDTO> create(@Valid @RequestBody LivreurDTO livreurDTO) {
        LivreurDTO created = livreurService.create(livreurDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Modifier un livreur")
    public ResponseEntity<LivreurDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody LivreurDTO livreurDTO) {
        LivreurDTO updated = livreurService.update(id, livreurDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Supprimer un livreur")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        livreurService.delete(id);
        return ResponseEntity.noContent().build();
    }
}