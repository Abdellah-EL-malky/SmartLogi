package org.example.smartlogi.repository;

import org.example.smartlogi.entity.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {

    Optional<Zone> findByNom(String nom);

    List<Zone> findByVille(String ville);

    Optional<Zone> findByCodePostal(String codePostal);

    boolean existsByNom(String nom);
}