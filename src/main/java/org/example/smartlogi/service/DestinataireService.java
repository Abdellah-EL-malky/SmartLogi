package org.example.smartlogi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartlogi.dto.DestinataireDTO;
import org.example.smartlogi.entity.Destinataire;
import org.example.smartlogi.mapper.DestinataireMapper;
import org.example.smartlogi.repository.DestinataireRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DestinataireService {

    private final DestinataireRepository destinataireRepository;
    private final DestinataireMapper destinataireMapper;

    public DestinataireDTO create(DestinataireDTO dto) {
        log.info("Création d'un nouveau destinataire : {} {}", dto.getPrenom(), dto.getNom());

        Destinataire entity = destinataireMapper.toEntity(dto);

        Destinataire savedEntity = destinataireRepository.save(entity);

        log.info("Destinataire créé avec succès - ID: {}", savedEntity.getId());

        return destinataireMapper.toDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public DestinataireDTO findById(Long id) {
        log.info("Recherche du destinataire avec l'ID: {}", id);

        Destinataire entity = destinataireRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Destinataire non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Destinataire non trouvé avec l'ID: " + id);
                });

        return destinataireMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<DestinataireDTO> findAll() {
        log.info("Récupération de tous les destinataires");

        List<Destinataire> entities = destinataireRepository.findAll();

        log.info("{} destinataires trouvés", entities.size());

        return destinataireMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public Page<DestinataireDTO> findAll(Pageable pageable) {
        log.info("Récupération des destinataires avec pagination - Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Destinataire> entities = destinataireRepository.findAll(pageable);

        return entities.map(destinataireMapper::toDTO);
    }

    public DestinataireDTO update(Long id, DestinataireDTO dto) {
        log.info("Mise à jour du destinataire avec l'ID: {}", id);

        Destinataire existingEntity = destinataireRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Destinataire non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Destinataire non trouvé avec l'ID: " + id);
                });

        destinataireMapper.updateEntityFromDTO(dto, existingEntity);

        Destinataire updatedEntity = destinataireRepository.save(existingEntity);

        log.info("Destinataire mis à jour avec succès - ID: {}", updatedEntity.getId());

        return destinataireMapper.toDTO(updatedEntity);
    }

    public void delete(Long id) {
        log.info("Suppression du destinataire avec l'ID: {}", id);

        if (!destinataireRepository.existsById(id)) {
            log.error("Destinataire non trouvé avec l'ID: {}", id);
            throw new RuntimeException("Destinataire non trouvé avec l'ID: " + id);
        }

        destinataireRepository.deleteById(id);

        log.info("Destinataire supprimé avec succès - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public DestinataireDTO findByEmail(String email) {
        log.info("Recherche du destinataire avec l'email: {}", email);

        Destinataire entity = destinataireRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Destinataire non trouvé avec l'email: {}", email);
                    return new RuntimeException("Destinataire non trouvé avec l'email: " + email);
                });

        return destinataireMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public DestinataireDTO findByTelephone(String telephone) {
        log.info("Recherche du destinataire avec le téléphone: {}", telephone);

        Destinataire entity = destinataireRepository.findByTelephone(telephone)
                .orElseThrow(() -> {
                    log.error("Destinataire non trouvé avec le téléphone: {}", telephone);
                    return new RuntimeException("Destinataire non trouvé avec le téléphone: " + telephone);
                });

        return destinataireMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<DestinataireDTO> searchByNom(String nom) {
        log.info("Recherche des destinataires contenant le nom: {}", nom);

        List<Destinataire> entities = destinataireRepository.findByNomContainingIgnoreCase(nom);

        log.info("{} destinataires trouvés", entities.size());

        return destinataireMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<DestinataireDTO> searchByVille(String ville) {
        log.info("Recherche des destinataires de la ville: {}", ville);

        List<Destinataire> entities = destinataireRepository.findByAdresseContainingIgnoreCase(ville);

        log.info("{} destinataires trouvés", entities.size());

        return destinataireMapper.toDTOList(entities);
    }
}