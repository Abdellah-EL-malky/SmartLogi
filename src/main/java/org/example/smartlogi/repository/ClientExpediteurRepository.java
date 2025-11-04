package org.example.smartlogi.repository;

import org.example.smartlogi.entity.ClientExpediteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientExpediteurRepository extends JpaRepository<ClientExpediteur, Long> {

    Optional<ClientExpediteur> findByEmail(String email);

    Optional<ClientExpediteur> findByTelephone(String telephone);

    List<ClientExpediteur> findByNomContainingIgnoreCase(String nom);

    boolean existsByEmail(String email);
}