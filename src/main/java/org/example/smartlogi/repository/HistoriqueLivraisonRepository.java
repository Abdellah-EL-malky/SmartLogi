package org.example.smartlogi.repository;

import org.example.smartlogi.entity.Colis;
import org.example.smartlogi.entity.HistoriqueLivraison;
import org.example.smartlogi.enums.StatutColis;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository pour l'entité HistoriqueLivraison
 */
@Repository
public interface HistoriqueLivraisonRepository extends JpaRepository<HistoriqueLivraison, Long> {

    List<HistoriqueLivraison> findByColisOrderByDateChangementAsc(Colis colis);

    List<HistoriqueLivraison> findByColisOrderByDateChangementDesc(Colis colis);

    List<HistoriqueLivraison> findByColisAndStatut(Colis colis, StatutColis statut);

    List<HistoriqueLivraison> findByStatut(StatutColis statut);

    List<HistoriqueLivraison> findByDateChangementBetween(LocalDateTime startDate, LocalDateTime endDate);

    Long countByColis(Colis colis);

    @Query("SELECT h FROM HistoriqueLivraison h WHERE h.colis = :colis ORDER BY h.dateChangement DESC")
    List<HistoriqueLivraison> findDernierChangement(@Param("colis") Colis colis);

    void deleteByColis(Colis colis);
}