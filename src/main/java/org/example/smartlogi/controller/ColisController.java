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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/colis")
@RequiredArgsConstructor
@Tag(name = "Colis", description = "API de gestion des colis")
public class ColisController {

    private final ColisService colisService;

    @PostMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'CLIENT')")
    @Operation(summary = "Créer un colis", description = "Créer un nouveau colis avec ses produits")
    public ResponseEntity<ColisDetailDTO> create(
            @Valid @RequestBody CreateColisRequest request,
            Authentication authentication) {

        String role = authentication.getAuthorities().iterator().next().getAuthority();

        ColisDetailDTO created = colisService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('MANAGER', 'DELIVERY_PERSON', 'CLIENT')")
    @Operation(summary = "Récupérer un colis", description = "Récupérer un colis par son ID")
    public ResponseEntity<ColisDTO> findById(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        ColisDTO colis = colisService.findById(id);

        if (role.equals("ROLE_MANAGER")) {
            return ResponseEntity.ok(colis);
        } else if (role.equals("ROLE_DELIVERY_PERSON")) {
            if (colisService.isAssignedToDeliveryPerson(id, username)) {
                return ResponseEntity.ok(colis);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            if (colisService.belongsToClient(id, username)) {
                return ResponseEntity.ok(colis);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/{id}/details")
    @PreAuthorize("hasAnyRole('MANAGER', 'DELIVERY_PERSON', 'CLIENT')")
    @Operation(summary = "Détails complets", description = "Récupérer tous les détails d'un colis")
    public ResponseEntity<ColisDetailDTO> findDetailById(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_MANAGER")) {
            ColisDetailDTO dto = colisService.findDetailById(id);
            return ResponseEntity.ok(dto);
        } else if (role.equals("ROLE_DELIVERY_PERSON")) {
            if (colisService.isAssignedToDeliveryPerson(id, username)) {
                ColisDetailDTO dto = colisService.findDetailById(id);
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            if (colisService.belongsToClient(id, username)) {
                ColisDetailDTO dto = colisService.findDetailById(id);
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('MANAGER', 'DELIVERY_PERSON', 'CLIENT')")
    @Operation(summary = "Lister tous les colis")
    public ResponseEntity<List<ColisDTO>> findAll(Authentication authentication) {
        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        List<ColisDTO> colis;

        if (role.equals("ROLE_MANAGER")) {
            colis = colisService.findAll();
        } else if (role.equals("ROLE_DELIVERY_PERSON")) {
            colis = colisService.findByLivreurUsername(username);
        } else {
            colis = colisService.findByClientUsername(username);
        }

        return ResponseEntity.ok(colis);
    }

    @GetMapping("/page")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Lister les colis (paginé)")
    public ResponseEntity<Page<ColisDTO>> findAllPaginated(Pageable pageable) {
        Page<ColisDTO> page = colisService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Supprimer un colis")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        colisService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/numero-suivi/{numeroSuivi}")
    @PreAuthorize("hasAnyRole('MANAGER', 'DELIVERY_PERSON', 'CLIENT')")
    @Operation(summary = "Rechercher par numéro de suivi")
    public ResponseEntity<ColisDetailDTO> findByNumeroSuivi(
            @PathVariable String numeroSuivi,
            Authentication authentication) {

        ColisDetailDTO dto = colisService.findByNumeroSuivi(numeroSuivi);

        String role = authentication.getAuthorities().iterator().next().getAuthority();
        String username = authentication.getName();

        if (role.equals("ROLE_MANAGER")) {
            return ResponseEntity.ok(dto);
        } else if (role.equals("ROLE_DELIVERY_PERSON")) {
            if (colisService.isAssignedToDeliveryPerson(dto.getId(), username)) {
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        } else {
            if (colisService.belongsToClient(dto.getId(), username)) {
                return ResponseEntity.ok(dto);
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/statut/{statut}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Rechercher par statut")
    public ResponseEntity<List<ColisDTO>> findByStatut(@PathVariable String statut) {
        List<ColisDTO> colis = colisService.findByStatut(statut);
        return ResponseEntity.ok(colis);
    }

    @PatchMapping("/{id}/statut")
    @PreAuthorize("hasAnyRole('MANAGER', 'DELIVERY_PERSON')")
    @Operation(summary = "Changer le statut")
    public ResponseEntity<ColisDTO> changerStatut(
            @PathVariable Long id,
            @RequestParam String statut,
            @RequestParam(required = false) String commentaire,
            Authentication authentication) {

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (role.equals("ROLE_DELIVERY_PERSON")) {
            if (!colisService.isAssignedToDeliveryPerson(id, username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }
        }

        ColisDTO dto = colisService.changerStatut(id, statut, commentaire);
        return ResponseEntity.ok(dto);
    }

    @PatchMapping("/{colisId}/assigner-livreur/{livreurId}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Assigner un livreur")
    public ResponseEntity<ColisDTO> assignerLivreur(
            @PathVariable Long colisId,
            @PathVariable Long livreurId) {
        ColisDTO dto = colisService.assignerLivreur(colisId, livreurId);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/client/{clientId}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Colis d'un client")
    public ResponseEntity<List<ColisDTO>> findByClient(@PathVariable Long clientId) {
        List<ColisDTO> colis = colisService.findByClient(clientId);
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/livreur/{livreurId}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Colis d'un livreur")
    public ResponseEntity<List<ColisDTO>> findByLivreur(@PathVariable Long livreurId) {
        List<ColisDTO> colis = colisService.findByLivreur(livreurId);
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/zone/{zoneId}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Colis d'une zone")
    public ResponseEntity<List<ColisDTO>> findByZone(@PathVariable Long zoneId) {
        List<ColisDTO> colis = colisService.findByZone(zoneId);
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/en-retard")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Colis en retard")
    public ResponseEntity<List<ColisDTO>> findColisEnRetard() {
        List<ColisDTO> colis = colisService.findColisEnRetard();
        return ResponseEntity.ok(colis);
    }

    @GetMapping("/{id}/historique")
    @PreAuthorize("hasAnyRole('MANAGER', 'DELIVERY_PERSON', 'CLIENT')")
    @Operation(summary = "Historique d'un colis")
    public ResponseEntity<List<HistoriqueLivraisonDTO>> getHistorique(
            @PathVariable Long id,
            Authentication authentication) {

        String username = authentication.getName();
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        if (!role.equals("ROLE_MANAGER")) {
            if (role.equals("ROLE_DELIVERY_PERSON")) {
                if (!colisService.isAssignedToDeliveryPerson(id, username)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            } else {
                if (!colisService.belongsToClient(id, username)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }
        }

        List<HistoriqueLivraisonDTO> historique = colisService.getHistorique(id);
        return ResponseEntity.ok(historique);
    }
}