package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.*;
import org.example.smartlogi.service.ColisService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
@Tag(name = "Colis", description = "API de gestion des colis")
public class ColisController {

    private final ColisService colisService;

    @PostMapping
    @Operation(summary = "Créer un colis", description = "Créer un nouveau colis avec ses produits")
    public ResponseEntity<ColisDetailDTO> create(@Valid @RequestBody CreateColisRequest request) {
        ColisDetailDTO created = colisService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un colis", description = "Récupérer un colis par son ID")
    public ResponseEntity<ColisDTO> findById(@PathVariable Long id) {
        ColisDTO dto = colisService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/details")
    @Operation(summary = "Détails complets", description = "Récupérer tous les détails d'un colis")
    public ResponseEntity<ColisDetailDTO> findDetailById(@PathVariable Long id) {
        ColisDetailDTO dto = colisService.findDetailById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Lister tous les colis")
    public ResponseEntity<List<ColisDTO>> findAll() {
        List<ColisDTO> colis = colisService.findAll();
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/page")
    @Operation(summary = "Lister les colis (paginé)")
    public ResponseEntity<Page<ColisDTO>> findAllPaginated(Pageable pageable) {
        Page<ColisDTO> page = colisService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un colis")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        colisService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/numero-suivi/{numeroSuivi}")
    @Operation(summary = "Rechercher par numéro de suivi")
    public ResponseEntity<ColisDetailDTO> findByNumeroSuivi(@PathVariable String numeroSuivi) {
        ColisDetailDTO dto = colisService.findByNumeroSuivi(numeroSuivi);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/statut/{statut}")
    @Operation(summary = "Rechercher par statut")
    public ResponseEntity<List<ColisDTO>> findByStatut(@PathVariable String statut) {
        List<ColisDTO> colis = colisService.findByStatut(statut);
        return ResponseEntity.ok(colis);
    }

    @PatchMapping("/{id}/statut")
    @Operation(summary = "Changer le statut")
    public ResponseEntity<ColisDTO> changerStatut(
            @PathVariable Long id,
            @RequestParam String statut,
            @RequestParam(required = false) String commentaire) {
        ColisDTO dto = colisService.changerStatut(id, statut, commentaire);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{colisId}/assigner-livreur/{livreurId}")
    @Operation(summary = "Assigner un livreur")
    public ResponseEntity<ColisDTO> assignerLivreur(
            @PathVariable Long colisId,
            @PathVariable Long livreurId) {
        ColisDTO dto = colisService.assignerLivreur(colisId, livreurId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/client/{clientId}")
    @Operation(summary = "Colis d'un client")
    public ResponseEntity<List<ColisDTO>> findByClient(@PathVariable Long clientId) {
        List<ColisDTO> colis = colisService.findByClient(clientId);
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/livreur/{livreurId}")
    @Operation(summary = "Colis d'un livreur")
    public ResponseEntity<List<ColisDTO>> findByLivreur(@PathVariable Long livreurId) {
        List<ColisDTO> colis = colisService.findByLivreur(livreurId);
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/zone/{zoneId}")
    @Operation(summary = "Colis d'une zone")
    public ResponseEntity<List<ColisDTO>> findByZone(@PathVariable Long zoneId) {
        List<ColisDTO> colis = colisService.findByZone(zoneId);
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/en-retard")
    @Operation(summary = "Colis en retard")
    public ResponseEntity<List<ColisDTO>> findColisEnRetard() {
        List<ColisDTO> colis = colisService.findColisEnRetard();
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/{id}/historique")
    @Operation(summary = "Historique d'un colis")
    public ResponseEntity<List<HistoriqueLivraisonDTO>> getHistorique(@PathVariable Long id) {
        List<HistoriqueLivraisonDTO> historique = colisService.getHistorique(id);
        return ResponseEntity.ok(historique);
    }
}