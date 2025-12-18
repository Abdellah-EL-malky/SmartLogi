package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.ClientExpediteurDTO;
import org.example.smartlogi.service.ClientExpediteurService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
@Tag(name = "Clients Expéditeurs", description = "API de gestion des clients")
public class ClientExpediteurController {

    private final ClientExpediteurService clientService;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Lister tous les clients")
    public ResponseEntity<List<ClientExpediteurDTO>> findAll() {
        List<ClientExpediteurDTO> clients = clientService.findAll();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Récupérer un client par ID")
    public ResponseEntity<ClientExpediteurDTO> findById(@PathVariable Long id) {
        ClientExpediteurDTO dto = clientService.findById(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau client")
    public ResponseEntity<ClientExpediteurDTO> create(@Valid @RequestBody ClientExpediteurDTO clientDTO) {
        ClientExpediteurDTO created = clientService.create(clientDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Modifier un client")
    public ResponseEntity<ClientExpediteurDTO> update(
            @PathVariable Long id,
            @Valid @RequestBody ClientExpediteurDTO clientDTO) {
        ClientExpediteurDTO updated = clientService.update(id, clientDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    @Operation(summary = "Supprimer un client")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }
}