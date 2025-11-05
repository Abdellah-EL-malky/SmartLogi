package org.example.smartlogi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartlogi.dto.ProduitDTO;
import org.example.smartlogi.entity.Produit;
import org.example.smartlogi.mapper.ProduitMapper;
import org.example.smartlogi.repository.ProduitRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;

    public ProduitDTO create(ProduitDTO dto) {
        log.info("Création d'un nouveau produit : {}", dto.getNom());

        Produit entity = produitMapper.toEntity(dto);

        Produit savedEntity = produitRepository.save(entity);

        log.info("Produit créé avec succès - ID: {}", savedEntity.getId());

        return produitMapper.toDTO(savedEntity);
    }

    @Transactional(readOnly = true)
    public ProduitDTO findById(Long id) {
        log.info("Recherche du produit avec l'ID: {}", id);

        Produit entity = produitRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Produit non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Produit non trouvé avec l'ID: " + id);
                });

        return produitMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ProduitDTO> findAll() {
        log.info("Récupération de tous les produits");

        List<Produit> entities = produitRepository.findAll();

        log.info("{} produits trouvés", entities.size());

        return produitMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public Page<ProduitDTO> findAll(Pageable pageable) {
        log.info("Récupération des produits avec pagination - Page: {}, Size: {}",
                pageable.getPageNumber(), pageable.getPageSize());

        Page<Produit> entities = produitRepository.findAll(pageable);

        return entities.map(produitMapper::toDTO);
    }

    public ProduitDTO update(Long id, ProduitDTO dto) {
        log.info("Mise à jour du produit avec l'ID: {}", id);

        Produit existingEntity = produitRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Produit non trouvé avec l'ID: {}", id);
                    return new RuntimeException("Produit non trouvé avec l'ID: " + id);
                });

        produitMapper.updateEntityFromDTO(dto, existingEntity);

        Produit updatedEntity = produitRepository.save(existingEntity);

        log.info("Produit mis à jour avec succès - ID: {}", updatedEntity.getId());

        return produitMapper.toDTO(updatedEntity);
    }

    public void delete(Long id) {
        log.info("Suppression du produit avec l'ID: {}", id);

        if (!produitRepository.existsById(id)) {
            log.error("Produit non trouvé avec l'ID: {}", id);
            throw new RuntimeException("Produit non trouvé avec l'ID: " + id);
        }

        produitRepository.deleteById(id);

        log.info("Produit supprimé avec succès - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public List<ProduitDTO> findByCategorie(String categorie) {
        log.info("Recherche des produits de la catégorie: {}", categorie);

        List<Produit> entities = produitRepository.findByCategorie(categorie);

        log.info("{} produits trouvés pour la catégorie {}", entities.size(), categorie);

        return produitMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<ProduitDTO> searchByNom(String nom) {
        log.info("Recherche des produits contenant le nom: {}", nom);

        List<Produit> entities = produitRepository.findByNomContainingIgnoreCase(nom);

        log.info("{} produits trouvés", entities.size());

        return produitMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<ProduitDTO> findByCategorieOrderByPrix(String categorie) {
        log.info("Recherche des produits de la catégorie {} triés par prix", categorie);

        List<Produit> entities = produitRepository.findByCategorieOrderByPrixAsc(categorie);

        log.info("{} produits trouvés", entities.size());

        return produitMapper.toDTOList(entities);
    }
}