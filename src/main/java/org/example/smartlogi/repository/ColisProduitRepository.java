package org.example.smartlogi.repository;

import org.example.smartlogi.entity.Colis;
import org.example.smartlogi.entity.ColisProduit;
import org.example.smartlogi.entity.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ColisProduitRepository extends JpaRepository<ColisProduit, Long> {

    List<ColisProduit> findByColis(Colis colis);

    List<ColisProduit> findByProduit(Produit produit);

    @Query("SELECT SUM(cp.prixUnitaire * cp.quantite) FROM ColisProduit cp WHERE cp.colis = :colis")
    BigDecimal calculerPrixTotalColis(@Param("colis") Colis colis);

    void deleteByColis(Colis colis);
}