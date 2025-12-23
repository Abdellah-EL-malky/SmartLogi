package org.example.smartlogi.service;

import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.PermissionDTO;
import org.example.smartlogi.dto.RoleDTO;
import org.example.smartlogi.entity.Permission;
import org.example.smartlogi.entity.Role;
import org.example.smartlogi.repository.PermissionRepository;
import org.example.smartlogi.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public List<RoleDTO> findAll() {
        return roleRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public RoleDTO findById(Long id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));
        return toDTO(role);
    }

    public RoleDTO findByName(String name) {
        Role role = roleRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Role not found with name: " + name));
        return toDTO(role);
    }

    @Transactional
    public RoleDTO create(RoleDTO roleDTO) {
        if (roleRepository.existsByName(roleDTO.getName())) {
            throw new RuntimeException("Role already exists with name: " + roleDTO.getName());
        }

        Role role = new Role();
        role.setName(roleDTO.getName());
        role.setDescription(roleDTO.getDescription());

        Role saved = roleRepository.save(role);
        return toDTO(saved);
    }

    @Transactional
    public RoleDTO update(Long id, RoleDTO roleDTO) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + id));

        role.setDescription(roleDTO.getDescription());

        Role updated = roleRepository.save(role);
        return toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!roleRepository.existsById(id)) {
            throw new RuntimeException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }

    @Transactional
    public RoleDTO assignPermission(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

        role.getPermissions().add(permission);
        Role updated = roleRepository.save(role);

        return toDTO(updated);
    }

    @Transactional
    public RoleDTO removePermission(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + permissionId));

        role.getPermissions().remove(permission);
        Role updated = roleRepository.save(role);

        return toDTO(updated);
    }

    public Set<PermissionDTO> getPermissions(Long roleId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new RuntimeException("Role not found with id: " + roleId));

        return role.getPermissions()
                .stream()
                .map(this::toPermissionDTO)
                .collect(Collectors.toSet());
    }

    private RoleDTO toDTO(Role role) {
        Set<PermissionDTO> permissions = role.getPermissions()
                .stream()
                .map(this::toPermissionDTO)
                .collect(Collectors.toSet());

        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .permissions(permissions)
                .createdAt(role.getCreatedAt())
                .build();
    }

    private PermissionDTO toPermissionDTO(Permission permission) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .createdAt(permission.getCreatedAt())
                .build();
    }
}