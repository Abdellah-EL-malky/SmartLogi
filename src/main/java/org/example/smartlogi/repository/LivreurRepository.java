package org.example.smartlogi.repository;

import org.example.smartlogi.entity.Livreur;
import org.example.smartlogi.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LivreurRepository extends JpaRepository<Livreur, Long> {

    Optional<Livreur> findByTelephone(String telephone);

    List<Livreur> findByZoneAssignee(Zone zone);

    List<Livreur> findByActif(Boolean actif);

    List<Livreur> findByZoneAssigneeAndActif(Zone zone, Boolean actif);

    List<Livreur> findByNomContainingIgnoreCase(String nom);

    List<Livreur> findByVehicule(String vehicule);

    @Query("SELECT COUNT(l) FROM Livreur l WHERE l.zoneAssignee = :zone AND l.actif = true")
    Long countLivreursActifsByZone(@Param("zone") Zone zone);
}