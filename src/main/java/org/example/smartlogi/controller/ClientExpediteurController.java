package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.ClientExpediteurDTO;
import org.example.smartlogi.service.ClientExpediteurService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients-expediteurs")
@RequiredArgsConstructor
@Tag(name = "Clients Expéditeurs", description = "API de gestion des clients expéditeurs")
public class ClientExpediteurController {

    private final ClientExpediteurService clientExpediteurService;

    @PostMapping
    @Operation(summary = "Créer un client expéditeur", description = "Créer un nouveau client expéditeur")
    public ResponseEntity<ClientExpediteurDTO> create(@Valid @RequestBody ClientExpediteurDTO dto) {
        ClientExpediteurDTO created = clientExpediteurService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un client", description = "Récupérer un client expéditeur par son ID")
    public ResponseEntity<ClientExpediteurDTO> findById(@PathVariable Long id) {
        ClientExpediteurDTO dto = clientExpediteurService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    @Operation(summary = "Lister tous les clients", description = "Récupérer la liste de tous les clients expéditeurs")
    public ResponseEntity<List<ClientExpediteurDTO>> findAll() {
        List<ClientExpediteurDTO> clients = clientExpediteurService.findAll();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/page")
    @Operation(summary = "Lister les clients (paginé)", description = "Récupérer les clients expéditeurs avec pagination")
    public ResponseEntity<Page<ClientExpediteurDTO>> findAllPaginated(Pageable pageable) {
        Page<ClientExpediteurDTO> page = clientExpediteurService.findAll(pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Mettre à jour un client existant
     */
    @PutMapping("/{id}")
    @Operation(summary = "Modifier un client", description = "Mettre à jour les informations d'un client expéditeur")
    public ResponseEntity<ClientExpediteurDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientExpediteurDTO dto) {
        ClientExpediteurDTO updated = clientExpediteurService.update(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un client", description = "Supprimer un client expéditeur par son ID")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientExpediteurService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Rechercher par email", description = "Trouver un client expéditeur par son email")
    public ResponseEntity<ClientExpediteurDTO> findByEmail(@PathVariable String email) {
        ClientExpediteurDTO dto = clientExpediteurService.findByEmail(email);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/telephone/{telephone}")
    @Operation(summary = "Rechercher par téléphone", description = "Trouver un client expéditeur par son téléphone")
    public ResponseEntity<ClientExpediteurDTO> findByTelephone(@PathVariable String telephone) {
        ClientExpediteurDTO dto = clientExpediteurService.findByTelephone(telephone);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher par nom", description = "Rechercher des clients expéditeurs par nom (recherche partielle)")
    public ResponseEntity<List<ClientExpediteurDTO>> searchByNom(@RequestParam String nom) {
        List<ClientExpediteurDTO> clients = clientExpediteurService.searchByNom(nom);
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/exists/email/{email}")
    @Operation(summary = "Vérifier l'existence d'un email", description = "Vérifier si un email existe déjà dans la base")
    public ResponseEntity<Boolean> emailExists(@PathVariable String email) {
        boolean exists = clientExpediteurService.emailExists(email);
        return ResponseEntity.ok(exists);
    }
}