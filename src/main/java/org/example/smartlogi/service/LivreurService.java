package org.example.smartlogi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartlogi.dto.LivreurDTO;
import org.example.smartlogi.entity.Livreur;
import org.example.smartlogi.entity.Zone;
import org.example.smartlogi.mapper.LivreurMapper;
import org.example.smartlogi.repository.LivreurRepository;
import org.example.smartlogi.repository.ZoneRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class LivreurService {

    private final LivreurRepository livreurRepository;
    private final ZoneRepository zoneRepository;
    private final LivreurMapper livreurMapper;

    public LivreurDTO create(LivreurDTO dto) {
        log.info("Création d'un nouveau livreur : {} {}", dto.getPrenom(), dto.getNom());

        if (livreurRepository.findByTelephone(dto.getTelephone()).isPresent()) {
            log.error("Le téléphone {} existe déjà", dto.getTelephone());
            throw new IllegalArgumentException("Un livreur avec ce numéro de téléphone existe déjà");
        }

        Zone zone = zoneRepository.findById(dto.getZoneAssigneeId())
                .orElseThrow(() -> {
                    log.error("Zone non trouvée avec l'ID: {}", dto.getZoneAssigneeId());
                    return new IllegalArgumentException("Zone non trouvée avec l'ID: " + dto.getZoneAssigneeId());
                });

        Livreur entity = livreurMapper.toEntity(dto);
        entity.setZoneAssignee(zone);

        Livreur savedEntity = livreurRepository.save(entity);

        log.info("Livreur créé avec succès - ID: {}, Zone: {}", savedEntity.getId(), zone.getNom());

        return livreurMapper.toDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public LivreurDTO findById(Long id) {
        log.info("Recherche du livreur avec l'ID: {}", id);

        Livreur entity = livreurRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Livreur non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Livreur non trouvé avec l'ID: " + id);
                });

        return livreurMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LivreurDTO> findAll() {
        log.info("Récupération de tous les livreurs");

        List<Livreur> entities = livreurRepository.findAll();

        log.info("{} livreurs trouvés", entities.size());

        return livreurMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public Page<LivreurDTO> findAll(Pageable pageable) {
        log.info("Récupération des livreurs avec pagination - Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Livreur> entities = livreurRepository.findAll(pageable);

        return entities.map(livreurMapper::toDTO);
    }

    public LivreurDTO update(Long id, LivreurDTO dto) {
        log.info("Mise à jour du livreur avec l'ID: {}", id);

        Livreur existingEntity = livreurRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Livreur non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Livreur non trouvé avec l'ID: " + id);
                });

        if (!existingEntity.getTelephone().equals(dto.getTelephone()) &&
                livreurRepository.findByTelephone(dto.getTelephone()).isPresent()) {
            log.error("Le téléphone {} existe déjà", dto.getTelephone());
            throw new IllegalArgumentException("Un livreur avec ce numéro de téléphone existe déjà");
        }

        Zone zone = zoneRepository.findById(dto.getZoneAssigneeId())
                .orElseThrow(() -> {
                    log.error("Zone non trouvée avec l'ID: {}", dto.getZoneAssigneeId());
                    return new IllegalArgumentException("Zone non trouvée avec l'ID: " + dto.getZoneAssigneeId());
                });

        livreurMapper.updateEntityFromDTO(dto, existingEntity);
        existingEntity.setZoneAssignee(zone);

        Livreur updatedEntity = livreurRepository.save(existingEntity);

        log.info("Livreur mis à jour avec succès - ID: {}", updatedEntity.getId());

        return livreurMapper.toDTO(updatedEntity);
    }

    public void delete(Long id) {
        log.info("Suppression du livreur avec l'ID: {}", id);

        if (!livreurRepository.existsById(id)) {
            log.error("Livreur non trouvé avec l'ID: {}", id);
            throw new RuntimeException("Livreur non trouvé avec l'ID: " + id);
        }

        livreurRepository.deleteById(id);

        log.info("Livreur supprimé avec succès - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public LivreurDTO findByTelephone(String telephone) {
        log.info("Recherche du livreur avec le téléphone: {}", telephone);

        Livreur entity = livreurRepository.findByTelephone(telephone)
                .orElseThrow(() -> {
                    log.error("Livreur non trouvé avec le téléphone: {}", telephone);
                    return new RuntimeException("Livreur non trouvé avec le téléphone: " + telephone);
                });

        return livreurMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<LivreurDTO> findByZone(Long zoneId) {
        log.info("Recherche des livreurs de la zone avec l'ID: {}", zoneId);

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> {
                    log.error("Zone non trouvée avec l'ID: {}", zoneId);
                    return new RuntimeException("Zone non trouvée avec l'ID: " + zoneId);
                });

        List<Livreur> entities = livreurRepository.findByZoneAssignee(zone);

        log.info("{} livreurs trouvés pour la zone {}", entities.size(), zone.getNom());

        return livreurMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<LivreurDTO> findActifs() {
        log.info("Recherche des livreurs actifs");

        List<Livreur> entities = livreurRepository.findByActif(true);

        log.info("{} livreurs actifs trouvés", entities.size());

        return livreurMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<LivreurDTO> findActifsByZone(Long zoneId) {
        log.info("Recherche des livreurs actifs de la zone avec l'ID: {}", zoneId);

        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> {
                    log.error("Zone non trouvée avec l'ID: {}", zoneId);
                    return new RuntimeException("Zone non trouvée avec l'ID: " + zoneId);
                });

        List<Livreur> entities = livreurRepository.findByZoneAssigneeAndActif(zone, true);

        log.info("{} livreurs actifs trouvés pour la zone {}", entities.size(), zone.getNom());

        return livreurMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<LivreurDTO> searchByNom(String nom) {
        log.info("Recherche des livreurs contenant le nom: {}", nom);

        List<Livreur> entities = livreurRepository.findByNomContainingIgnoreCase(nom);

        log.info("{} livreurs trouvés", entities.size());

        return livreurMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<LivreurDTO> findByVehicule(String vehicule) {
        log.info("Recherche des livreurs avec le véhicule: {}", vehicule);

        List<Livreur> entities = livreurRepository.findByVehicule(vehicule);

        log.info("{} livreurs trouvés", entities.size());

        return livreurMapper.toDTOList(entities);
    }

    public LivreurDTO setActif(Long id, boolean actif) {
        log.info("Changement du statut du livreur {} à {}", id, actif ? "actif" : "inactif");

        Livreur entity = livreurRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Livreur non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Livreur non trouvé avec l'ID: " + id);
                });

        entity.setActif(actif);
        Livreur savedEntity = livreurRepository.save(entity);

        log.info("Statut du livreur {} changé avec succès", id);

        return livreurMapper.toDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public Long countActifsByZone(Long zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée avec l'ID: " + zoneId));

        return livreurRepository.countLivreursActifsByZone(zone);
    }
}