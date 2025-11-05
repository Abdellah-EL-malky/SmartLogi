package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.DestinataireDTO;
import org.example.smartlogi.service.DestinataireService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/destinataires")
@RequiredArgsConstructor
@Tag(name = "Destinataires", description = "API de gestion des destinataires")
public class DestinataireController {

    private final DestinataireService destinataireService;

    @PostMapping
    @Operation(summary = "Créer un destinataire", description = "Créer un nouveau destinataire")
    public ResponseEntity<DestinataireDTO> create(@Valid @RequestBody DestinataireDTO dto) {
        DestinataireDTO created = destinataireService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un destinataire", description = "Récupérer un destinataire par son ID")
    public ResponseEntity<DestinataireDTO> findById(@PathVariable Long id) {
        DestinataireDTO dto = destinataireService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Lister tous les destinataires", description = "Récupérer la liste de tous les destinataires")
    public ResponseEntity<List<DestinataireDTO>> findAll() {
        List<DestinataireDTO> destinataires = destinataireService.findAll();
        return ResponseEntity.ok(destinataires);
    }

    @GetMapping("/page")
    @Operation(summary = "Lister les destinataires (paginé)", description = "Récupérer les destinataires avec pagination")
    public ResponseEntity<Page<DestinataireDTO>> findAllPaginated(Pageable pageable) {
        Page<DestinataireDTO> page = destinataireService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modifier un destinataire", description = "Mettre à jour les informations d'un destinataire")
    public ResponseEntity<DestinataireDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody DestinataireDTO dto) {
        DestinataireDTO updated = destinataireService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un destinataire", description = "Supprimer un destinataire par son ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        destinataireService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Rechercher par email", description = "Trouver un destinataire par son email")
    public ResponseEntity<DestinataireDTO> findByEmail(@PathVariable String email) {
        DestinataireDTO dto = destinataireService.findByEmail(email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/telephone/{telephone}")
    @Operation(summary = "Rechercher par téléphone", description = "Trouver un destinataire par son téléphone")
    public ResponseEntity<DestinataireDTO> findByTelephone(@PathVariable String telephone) {
        DestinataireDTO dto = destinataireService.findByTelephone(telephone);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search/nom")
    @Operation(summary = "Rechercher par nom", description = "Rechercher des destinataires par nom (recherche partielle)")
    public ResponseEntity<List<DestinataireDTO>> searchByNom(@RequestParam String nom) {
        List<DestinataireDTO> destinataires = destinataireService.searchByNom(nom);
        return ResponseEntity.ok(destinataires);
    }

    @GetMapping("/search/ville")
    @Operation(summary = "Rechercher par ville", description = "Rechercher des destinataires par ville (dans l'adresse)")
    public ResponseEntity<List<DestinataireDTO>> searchByVille(@RequestParam String ville) {
        List<DestinataireDTO> destinataires = destinataireService.searchByVille(ville);
        return ResponseEntity.ok(destinataires);
    }
}