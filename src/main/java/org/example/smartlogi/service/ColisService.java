package org.example.smartlogi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.smartlogi.dto.*;
import org.example.smartlogi.entity.*;
import org.example.smartlogi.enums.PrioriteColis;
import org.example.smartlogi.enums.StatutColis;
import org.example.smartlogi.mapper.ColisMapper;
import org.example.smartlogi.repository.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ColisService {

    private final ColisRepository colisRepository;
    private final ClientExpediteurRepository clientExpediteurRepository;
    private final DestinataireRepository destinataireRepository;
    private final ZoneRepository zoneRepository;
    private final LivreurRepository livreurRepository;
    private final ProduitRepository produitRepository;
    private final ColisProduitRepository colisProduitRepository;
    private final HistoriqueLivraisonRepository historiqueLivraisonRepository;
    private final ColisMapper colisMapper;

    public ColisDetailDTO create(CreateColisRequest request) {
        log.info("Création d'un nouveau colis");

        ClientExpediteur client = clientExpediteurRepository.findById(request.getClientExpediteurId())
                .orElseThrow(() -> new RuntimeException("Client expéditeur non trouvé"));

        Destinataire destinataire = destinataireRepository.findById(request.getDestinataireId())
                .orElseThrow(() -> new RuntimeException("Destinataire non trouvé"));

        Zone zone = zoneRepository.findById(request.getZoneDestinationId())
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));

        Colis colis = new Colis();
        colis.setNumeroSuivi(genererNumeroSuivi());
        colis.setClientExpediteur(client);
        colis.setDestinataire(destinataire);
        colis.setZone(zone);
        colis.setVilleDestination(zone.getVille());
        colis.setPriorite(PrioriteColis.valueOf(request.getPriorite()));
        colis.setStatut(StatutColis.CREE);
        colis.setDateLivraisonPrevue(request.getDateLivraisonPrevue());
        colis.setDescription(request.getCommentaire());

        BigDecimal poidsTotal = BigDecimal.ZERO;
        List<ColisProduit> colisProduits = new ArrayList<>();

        for (CreateColisProduitRequest produitRequest : request.getProduits()) {
            Produit produit = produitRepository.findById(produitRequest.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit non trouvé"));

            ColisProduit colisProduit = new ColisProduit();
            colisProduit.setColis(colis);
            colisProduit.setProduit(produit);
            colisProduit.setQuantite(produitRequest.getQuantite());
            colisProduit.setPrixUnitaire(produit.getPrix());

            poidsTotal = poidsTotal.add(produit.getPoids().multiply(BigDecimal.valueOf(produitRequest.getQuantite())));
            colisProduits.add(colisProduit);
        }

        colis.setPoidsTotal(poidsTotal);
        colis.setProduits(colisProduits);

        Colis savedColis = colisRepository.save(colis);
        creerHistorique(savedColis, StatutColis.CREE, "Colis créé");

        log.info("Colis créé - Numéro: {}", savedColis.getNumeroSuivi());
        return colisMapper.toDetailDTO(savedColis);
    }

    @Transactional(readOnly = true)
    public ColisDTO findById(Long id) {
        Colis entity = colisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        return colisMapper.toDTO(entity);
    }

    @Transactional(readOnly = true)
    public ColisDetailDTO findDetailById(Long id) {
        Colis entity = colisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        return colisMapper.toDetailDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ColisDTO> findAll() {
        List<Colis> entities = colisRepository.findAll();
        return colisMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public Page<ColisDTO> findAll(Pageable pageable) {
        Page<Colis> entities = colisRepository.findAll(pageable);
        return entities.map(colisMapper::toDTO);
    }

    public ColisDTO changerStatut(Long id, String nouveauStatut, String commentaire) {
        log.info("Changement de statut du colis {} vers {}", id, nouveauStatut);

        Colis colis = colisRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        StatutColis statut = StatutColis.valueOf(nouveauStatut);
        colis.setStatut(statut);

        if (statut == StatutColis.LIVRE) {
            colis.setDateLivraisonEffective(LocalDateTime.now());
        }

        Colis savedColis = colisRepository.save(colis);
        creerHistorique(savedColis, statut, commentaire);

        return colisMapper.toDTO(savedColis);
    }

    public ColisDTO assignerLivreur(Long colisId, Long livreurId) {
        log.info("Assignment du livreur {} au colis {}", livreurId, colisId);

        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));

        colis.setLivreur(livreur);
        colis.setStatut(StatutColis.EN_TRANSIT);

        Colis savedColis = colisRepository.save(colis);
        creerHistorique(savedColis, StatutColis.EN_TRANSIT, "Assigné au livreur " + livreur.getNom());

        return colisMapper.toDTO(savedColis);
    }

    public void delete(Long id) {
        if (!colisRepository.existsById(id)) {
            throw new RuntimeException("Colis non trouvé");
        }
        colisRepository.deleteById(id);
        log.info("Colis supprimé - ID: {}", id);
    }

    @Transactional(readOnly = true)
    public ColisDetailDTO findByNumeroSuivi(String numeroSuivi) {
        Colis entity = colisRepository.findByNumeroSuivi(numeroSuivi)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));
        return colisMapper.toDetailDTO(entity);
    }

    @Transactional(readOnly = true)
    public List<ColisDTO> findByStatut(String statut) {
        List<Colis> entities = colisRepository.findByStatut(StatutColis.valueOf(statut));
        return colisMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<ColisDTO> findByClient(Long clientId) {
        ClientExpediteur client = clientExpediteurRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client non trouvé"));
        List<Colis> entities = colisRepository.findByClientExpediteur(client);
        return colisMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<ColisDTO> findByLivreur(Long livreurId) {
        Livreur livreur = livreurRepository.findById(livreurId)
                .orElseThrow(() -> new RuntimeException("Livreur non trouvé"));
        List<Colis> entities = colisRepository.findByLivreur(livreur);
        return colisMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<ColisDTO> findByZone(Long zoneId) {
        Zone zone = zoneRepository.findById(zoneId)
                .orElseThrow(() -> new RuntimeException("Zone non trouvée"));
        List<Colis> entities = colisRepository.findByZone(zone);
        return colisMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<ColisDTO> findColisEnRetard() {
        List<Colis> entities = colisRepository.findColisEnRetard(LocalDateTime.now());
        return colisMapper.toDTOList(entities);
    }

    @Transactional(readOnly = true)
    public List<HistoriqueLivraisonDTO> getHistorique(Long colisId) {
        Colis colis = colisRepository.findById(colisId)
                .orElseThrow(() -> new RuntimeException("Colis non trouvé"));

        List<HistoriqueLivraison> historiques = historiqueLivraisonRepository
                .findByColisOrderByDateChangementDesc(colis);

        return historiques.stream()
                .map(h -> {
                    HistoriqueLivraisonDTO dto = new HistoriqueLivraisonDTO();
                    dto.setId(h.getId());
                    dto.setColisId(h.getColis().getId());
                    dto.setStatut(h.getStatut().name());
                    dto.setCommentaire(h.getCommentaire());
                    dto.setDateChangement(h.getDateChangement());
                    return dto;
                })
                .toList();
    }

    private String genererNumeroSuivi() {
        String numero;
        do {
            numero = "COL-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        } while (colisRepository.existsByNumeroSuivi(numero));
        return numero;
    }

    private void creerHistorique(Colis colis, StatutColis statut, String commentaire) {
        HistoriqueLivraison historique = new HistoriqueLivraison();
        historique.setColis(colis);
        historique.setStatut(statut);
        historique.setCommentaire(commentaire);
        historiqueLivraisonRepository.save(historique);
    }

    //
}