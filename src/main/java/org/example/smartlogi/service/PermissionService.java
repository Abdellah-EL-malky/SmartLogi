package org.example.smartlogi.service;

import lombok.RequiredArgsConstructor;
import org.example.smartlogi.dto.PermissionDTO;
import org.example.smartlogi.entity.Permission;
import org.example.smartlogi.repository.PermissionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public List<PermissionDTO> findAll() {
        return permissionRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PermissionDTO findById(Long id) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));
        return toDTO(permission);
    }

    public PermissionDTO findByName(String name) {
        Permission permission = permissionRepository.findByName(name)
                .orElseThrow(() -> new RuntimeException("Permission not found with name: " + name));
        return toDTO(permission);
    }

    @Transactional
    public PermissionDTO create(PermissionDTO permissionDTO) {
        if (permissionRepository.existsByName(permissionDTO.getName())) {
            throw new RuntimeException("Permission already exists with name: " + permissionDTO.getName());
        }

        Permission permission = new Permission();
        permission.setName(permissionDTO.getName());
        permission.setDescription(permissionDTO.getDescription());

        Permission saved = permissionRepository.save(permission);
        return toDTO(saved);
    }

    @Transactional
    public PermissionDTO update(Long id, PermissionDTO permissionDTO) {
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Permission not found with id: " + id));

        permission.setDescription(permissionDTO.getDescription());

        Permission updated = permissionRepository.save(permission);
        return toDTO(updated);
    }

    @Transactional
    public void delete(Long id) {
        if (!permissionRepository.existsById(id)) {
            throw new RuntimeException("Permission not found with id: " + id);
        }
        permissionRepository.deleteById(id);
    }

    private PermissionDTO toDTO(Permission permission) {
        return PermissionDTO.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .createdAt(permission.getCreatedAt())
                .build();
    }
}