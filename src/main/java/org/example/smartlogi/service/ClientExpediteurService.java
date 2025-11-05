package org.example.smartlogi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartlogi.dto.ClientExpediteurDTO;
import org.example.smartlogi.entity.ClientExpediteur;
import org.example.smartlogi.mapper.ClientExpediteurMapper;
import org.example.smartlogi.repository.ClientExpediteurRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ClientExpediteurService {

    private final ClientExpediteurRepository clientExpediteurRepository;
    private final ClientExpediteurMapper clientExpediteurMapper;

    public ClientExpediteurDTO create(ClientExpediteurDTO dto) {
        log.info("Création d'un nouveau client expéditeur : {}", dto.getEmail());

        if (clientExpediteurRepository.existsByEmail(dto.getEmail())) {
            log.error("L'email {} existe déjà", dto.getEmail());
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        ClientExpediteur entity = clientExpediteurMapper.toEntity(dto);

        ClientExpediteur savedEntity = clientExpediteurRepository.save(entity);

        log.info("Client expéditeur créé avec succès - ID: {}", savedEntity.getId());

        return clientExpediteurMapper.toDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public ClientExpediteurDTO findById(Long id) {
        log.info("Recherche du client expéditeur avec l'ID: {}", id);

        ClientExpediteur entity = clientExpediteurRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Client expéditeur non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Client expéditeur non trouvé avec l'ID: " + id);
                });

        return clientExpediteurMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ClientExpediteurDTO> findAll() {
        log.info("Récupération de tous les clients expéditeurs");

        List<ClientExpediteur> entities = clientExpediteurRepository.findAll();

        log.info("{} clients expéditeurs trouvés", entities.size());

        return clientExpediteurMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public Page<ClientExpediteurDTO> findAll(Pageable pageable) {
        log.info("Récupération des clients expéditeurs avec pagination - Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<ClientExpediteur> entities = clientExpediteurRepository.findAll(pageable);

        return entities.map(clientExpediteurMapper::toDTO);
    }

    public ClientExpediteurDTO update(Long id, ClientExpediteurDTO dto) {
        log.info("Mise à jour du client expéditeur avec l'ID: {}", id);

        ClientExpediteur existingEntity = clientExpediteurRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Client expéditeur non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Client expéditeur non trouvé avec l'ID: " + id);
                });

        if (!existingEntity.getEmail().equals(dto.getEmail()) &&
                clientExpediteurRepository.existsByEmail(dto.getEmail())) {
            log.error("L'email {} existe déjà", dto.getEmail());
            throw new IllegalArgumentException("Un client avec cet email existe déjà");
        }

        clientExpediteurMapper.updateEntityFromDTO(dto, existingEntity);

        ClientExpediteur updatedEntity = clientExpediteurRepository.save(existingEntity);

        log.info("Client expéditeur mis à jour avec succès - ID: {}", updatedEntity.getId());

        return clientExpediteurMapper.toDTO(updatedEntity);
    }

    public void delete(Long id) {
        log.info("Suppression du client expéditeur avec l'ID: {}", id);

        if (!clientExpediteurRepository.existsById(id)) {
            log.error("Client expéditeur non trouvé avec l'ID: {}", id);
            throw new RuntimeException("Client expéditeur non trouvé avec l'ID: " + id);
        }

        clientExpediteurRepository.deleteById(id);

        log.info("Client expéditeur supprimé avec succès - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public ClientExpediteurDTO findByEmail(String email) {
        log.info("Recherche du client expéditeur avec l'email: {}", email);

        ClientExpediteur entity = clientExpediteurRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Client expéditeur non trouvé avec l'email: {}", email);
                    return new RuntimeException("Client expéditeur non trouvé avec l'email: " + email);
                });

        return clientExpediteurMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public ClientExpediteurDTO findByTelephone(String telephone) {
        log.info("Recherche du client expéditeur avec le téléphone: {}", telephone);

        ClientExpediteur entity = clientExpediteurRepository.findByTelephone(telephone)
                .orElseThrow(() -> {
                    log.error("Client expéditeur non trouvé avec le téléphone: {}", telephone);
                    return new RuntimeException("Client expéditeur non trouvé avec le téléphone: " + telephone);
                });

        return clientExpediteurMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ClientExpediteurDTO> searchByNom(String nom) {
        log.info("Recherche des clients expéditeurs contenant le nom: {}", nom);

        List<ClientExpediteur> entities = clientExpediteurRepository.findByNomContainingIgnoreCase(nom);

        log.info("{} clients expéditeurs trouvés", entities.size());

        return clientExpediteurMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public boolean emailExists(String email) {
        return clientExpediteurRepository.existsByEmail(email);
    }
}