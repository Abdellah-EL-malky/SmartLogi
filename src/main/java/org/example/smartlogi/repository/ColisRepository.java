package org.example.smartlogi.repository;

import org.example.smartlogi.entity.*;
import org.example.smartlogi.enums.PrioriteColis;
import org.example.smartlogi.enums.StatutColis;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Colis
 */
@Repository
public interface ColisRepository extends JpaRepository<Colis, Long> {

    // ========================================
    // RECHERCHES SIMPLES
    // ========================================

    Optional<Colis> findByNumeroSuivi(String numeroSuivi);

    boolean existsByNumeroSuivi(String numeroSuivi);

    List<Colis> findByStatut(StatutColis statut);

    List<Colis> findByPriorite(PrioriteColis priorite);

    List<Colis> findByVilleDestination(String villeDestination);

    // ========================================
    // RECHERCHES PAR RELATIONS
    // ========================================

    List<Colis> findByClientExpediteur(ClientExpediteur clientExpediteur);

    List<Colis> findByDestinataire(Destinataire destinataire);

    List<Colis> findByLivreur(Livreur livreur);

    List<Colis> findByLivreurIsNull();

    List<Colis> findByZone(Zone zone);

    // ========================================
    // RECHERCHES COMBINÉES
    // ========================================

    List<Colis> findByStatutAndZone(StatutColis statut, Zone zone);

    List<Colis> findByStatutAndPriorite(StatutColis statut, PrioriteColis priorite);

    List<Colis> findByLivreurAndStatut(Livreur livreur, StatutColis statut);

    List<Colis> findByZoneAndPriorite(Zone zone, PrioriteColis priorite);

    // ========================================
    // RECHERCHES PAR DATE
    // ========================================

    List<Colis> findByDateLivraisonPrevueBefore(LocalDateTime date);

    List<Colis> findByDateCreationBetween(LocalDateTime startDate, LocalDateTime endDate);

    // ========================================
    // PAGINATION
    // ========================================

    Page<Colis> findByStatut(StatutColis statut, Pageable pageable);

    Page<Colis> findByZone(Zone zone, Pageable pageable);

    Page<Colis> findByClientExpediteur(ClientExpediteur clientExpediteur, Pageable pageable);

    // ========================================
    // QUERIES PERSONNALISÉES
    // ========================================

    @Query("SELECT c FROM Colis c WHERE c.dateLivraisonPrevue < :now AND c.statut <> 'LIVRE'")
    List<Colis> findColisEnRetard(@Param("now") LocalDateTime now);

    @Query("SELECT c FROM Colis c WHERE c.priorite IN ('URGENTE', 'TRES_URGENTE') AND c.livreur IS NULL")
    List<Colis> findColisPrioritairesNonAssignes();

    @Query("SELECT SUM(c.poidsTotal) FROM Colis c WHERE c.livreur = :livreur")
    BigDecimal calculerPoidsTotalParLivreur(@Param("livreur") Livreur livreur);

    @Query("SELECT SUM(c.poidsTotal) FROM Colis c WHERE c.zone = :zone")
    BigDecimal calculerPoidsTotalParZone(@Param("zone") Zone zone);

    Long countByStatut(StatutColis statut);

    Long countByZone(Zone zone);

    Long countByLivreur(Livreur livreur);

    Long countByStatutAndZone(StatutColis statut, Zone zone);

    @Query("SELECT c FROM Colis c WHERE c.livreur = :livreur AND c.statut IN ('EN_TRANSIT', 'CREE')")
    List<Colis> findColisEnCoursLivraison(@Param("livreur") Livreur livreur);

    @Query("SELECT c FROM Colis c WHERE " +
            "LOWER(c.numeroSuivi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.villeDestination) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Colis> rechercherParMotCle(@Param("keyword") String keyword);

    @Query("SELECT c FROM Colis c WHERE " +
            "LOWER(c.numeroSuivi) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(c.villeDestination) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Colis> rechercherParMotCle(@Param("keyword") String keyword, Pageable pageable);
}