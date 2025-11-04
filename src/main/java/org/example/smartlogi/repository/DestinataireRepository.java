package org.example.smartlogi.repository;

import org.example.smartlogi.entity.Destinataire;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DestinataireRepository extends JpaRepository<Destinataire, Long> {

    Optional<Destinataire> findByEmail(String email);

    Optional<Destinataire> findByTelephone(String telephone);

    List<Destinataire> findByNomContainingIgnoreCase(String nom);

    List<Destinataire> findByAdresseContainingIgnoreCase(String ville);
}