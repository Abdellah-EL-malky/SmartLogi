package org.example.smartlogi.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.PermissionDTO;
import org.example.smartlogi.dto.RoleDTO;
import org.example.smartlogi.service.PermissionService;
import org.example.smartlogi.service.RoleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('MANAGER')")
@Tag(name = "Admin", description = "API d'administration des rôles et permissions")
public class AdminController {

    private final PermissionService permissionService;
    private final RoleService roleService;

    @GetMapping("/permissions")
    @Operation(summary = "Lister toutes les permissions")
    public ResponseEntity<List<PermissionDTO>> getAllPermissions() {
        List<PermissionDTO> permissions = permissionService.findAll();
        return ResponseEntity.ok(permissions);
    }

    @GetMapping("/permissions/{id}")
    @Operation(summary = "Récupérer une permission")
    public ResponseEntity<PermissionDTO> getPermissionById(@PathVariable Long id) {
        PermissionDTO permission = permissionService.findById(id);
        return ResponseEntity.ok(permission);
    }

    @PostMapping("/permissions")
    @Operation(summary = "Créer une permission")
    public ResponseEntity<PermissionDTO> createPermission(@Valid @RequestBody PermissionDTO permissionDTO) {
        PermissionDTO created = permissionService.create(permissionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/permissions/{id}")
    @Operation(summary = "Modifier une permission")
    public ResponseEntity<PermissionDTO> updatePermission(
            @PathVariable Long id,
            @Valid @RequestBody PermissionDTO permissionDTO) {
        PermissionDTO updated = permissionService.update(id, permissionDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/permissions/{id}")
    @Operation(summary = "Supprimer une permission")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) {
        permissionService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/roles")
    @Operation(summary = "Lister tous les rôles")
    public ResponseEntity<List<RoleDTO>> getAllRoles() {
        List<RoleDTO> roles = roleService.findAll();
        return ResponseEntity.ok(roles);
    }

    @GetMapping("/roles/{id}")
    @Operation(summary = "Récupérer un rôle")
    public ResponseEntity<RoleDTO> getRoleById(@PathVariable Long id) {
        RoleDTO role = roleService.findById(id);
        return ResponseEntity.ok(role);
    }

    @PostMapping("/roles")
    @Operation(summary = "Créer un rôle")
    public ResponseEntity<RoleDTO> createRole(@Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO created = roleService.create(roleDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/roles/{id}")
    @Operation(summary = "Modifier un rôle")
    public ResponseEntity<RoleDTO> updateRole(
            @PathVariable Long id,
            @Valid @RequestBody RoleDTO roleDTO) {
        RoleDTO updated = roleService.update(id, roleDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/roles/{id}")
    @Operation(summary = "Supprimer un rôle")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        roleService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/roles/{roleId}/permissions/{permissionId}")
    @Operation(summary = "Assigner une permission à un rôle")
    public ResponseEntity<RoleDTO> assignPermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        RoleDTO updated = roleService.assignPermission(roleId, permissionId);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/roles/{roleId}/permissions/{permissionId}")
    @Operation(summary = "Retirer une permission d'un rôle")
    public ResponseEntity<RoleDTO> removePermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        RoleDTO updated = roleService.removePermission(roleId, permissionId);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/roles/{roleId}/permissions")
    @Operation(summary = "Consulter les permissions d'un rôle")
    public ResponseEntity<Set<PermissionDTO>> getRolePermissions(@PathVariable Long roleId) {
        Set<PermissionDTO> permissions = roleService.getPermissions(roleId);
        return ResponseEntity.ok(permissions);
    }
}