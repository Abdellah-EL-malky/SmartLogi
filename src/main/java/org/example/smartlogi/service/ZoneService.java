package org.example.smartlogi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartlogi.dto.ZoneDTO;
import org.example.smartlogi.entity.Zone;
import org.example.smartlogi.mapper.ZoneMapper;
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
public class ZoneService {

    private final ZoneRepository zoneRepository;
    private final ZoneMapper zoneMapper;

    public ZoneDTO create(ZoneDTO dto) {
        log.info("Création d'une nouvelle zone : {}", dto.getNom());

        if (zoneRepository.existsByNom(dto.getNom())) {
            log.error("Le nom de zone {} existe déjà", dto.getNom());
            throw new IllegalArgumentException("Une zone avec ce nom existe déjà");
        }

        Zone entity = zoneMapper.toEntity(dto);

        Zone savedEntity = zoneRepository.save(entity);

        log.info("Zone créée avec succès - ID: {}", savedEntity.getId());

        return zoneMapper.toDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public ZoneDTO findById(Long id) {
        log.info("Recherche de la zone avec l'ID: {}", id);

        Zone entity = zoneRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Zone non trouvée avec l'ID: {}", id);
                    return new RuntimeException("Zone non trouvée avec l'ID: " + id);
                });

        return zoneMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ZoneDTO> findAll() {
        log.info("Récupération de toutes les zones");

        List<Zone> entities = zoneRepository.findAll();

        log.info("{} zones trouvées", entities.size());

        return zoneMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public Page<ZoneDTO> findAll(Pageable pageable) {
        log.info("Récupération des zones avec pagination - Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Zone> entities = zoneRepository.findAll(pageable);

        return entities.map(zoneMapper::toDTO);
    }

    public ZoneDTO update(Long id, ZoneDTO dto) {
        log.info("Mise à jour de la zone avec l'ID: {}", id);

        Zone existingEntity = zoneRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Zone non trouvée avec l'ID: {}", id);
                    return new RuntimeException("Zone non trouvée avec l'ID: " + id);
                });

        if (!existingEntity.getNom().equals(dto.getNom()) &&
                zoneRepository.existsByNom(dto.getNom())) {
            log.error("Le nom de zone {} existe déjà", dto.getNom());
            throw new IllegalArgumentException("Une zone avec ce nom existe déjà");
        }

        zoneMapper.updateEntityFromDTO(dto, existingEntity);

        Zone updatedEntity = zoneRepository.save(existingEntity);

        log.info("Zone mise à jour avec succès - ID: {}", updatedEntity.getId());

        return zoneMapper.toDTO(updatedEntity);
    }

    public void delete(Long id) {
        log.info("Suppression de la zone avec l'ID: {}", id);

        if (!zoneRepository.existsById(id)) {
            log.error("Zone non trouvée avec l'ID: {}", id);
            throw new RuntimeException("Zone non trouvée avec l'ID: " + id);
        }

        zoneRepository.deleteById(id);

        log.info("Zone supprimée avec succès - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public ZoneDTO findByNom(String nom) {
        log.info("Recherche de la zone avec le nom: {}", nom);

        Zone entity = zoneRepository.findByNom(nom)
                .orElseThrow(() -> {
                    log.error("Zone non trouvée avec le nom: {}", nom);
                    return new RuntimeException("Zone non trouvée avec le nom: " + nom);
                });

        return zoneMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ZoneDTO> findByVille(String ville) {
        log.info("Recherche des zones de la ville: {}", ville);

        List<Zone> entities = zoneRepository.findByVille(ville);

        log.info("{} zones trouvées pour la ville {}", entities.size(), ville);

        return zoneMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public ZoneDTO findByCodePostal(String codePostal) {
        log.info("Recherche de la zone avec le code postal: {}", codePostal);

        Zone entity = zoneRepository.findByCodePostal(codePostal)
                .orElseThrow(() -> {
                    log.error("Zone non trouvée avec le code postal: {}", codePostal);
                    return new RuntimeException("Zone non trouvée avec le code postal: " + codePostal);
                });

        return zoneMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public boolean existsByNom(String nom) {
        return zoneRepository.existsByNom(nom);
    }
}