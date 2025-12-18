package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.DestinataireDTO;
import org.example.smartlogi.service.DestinataireService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinataires")
@RequiredArgsConstructor
@Tag(name = "Destinataires", description = "API de gestion des destinataires")
public class DestinataireController {

    private final DestinataireService destinataireService;

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @Operation(summary = "Lister tous les destinataires")
    public ResponseEntity<List<DestinataireDTO>> findAll() {
        List<DestinataireDTO> destinataires = destinataireService.findAll();
        return ResponseEntity.ok(destinataires);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @Operation(summary = "Récupérer un destinataire par ID")
    public ResponseEntity<DestinataireDTO> findById(@PathVariable Long id) {
        DestinataireDTO dto = destinataireService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @Operation(summary = "Créer un nouveau destinataire")
    public ResponseEntity<DestinataireDTO> create(@Valid @RequestBody DestinataireDTO destinataireDTO) {
        DestinataireDTO created = destinataireService.create(destinataireDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @Operation(summary = "Modifier un destinataire")
    public ResponseEntity<DestinataireDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DestinataireDTO destinataireDTO) {
        DestinataireDTO updated = destinataireService.update(id, destinataireDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Supprimer un destinataire")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        destinataireService.delete(id);
        return ResponseEntity.noContent().build();
    }
}