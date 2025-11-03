package org.example.smartlogi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.smartlogi.enums.PrioriteColis;
import org.example.smartlogi.enums.StatutColis;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "colis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Colis {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "numero_suivi", nullable = false, unique = true, length = 50)
    private String numeroSuivi;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "poids_total", nullable = false, precision = 10, scale = 2)
    private BigDecimal poidsTotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false, length = 20)
    private StatutColis statut;

    @Enumerated(EnumType.STRING)
    @Column(name = "priorite", nullable = false, length = 20)
    private PrioriteColis priorite;

    @Column(name = "ville_destination", nullable = false, length = 100)
    private String villeDestination;

    @CreationTimestamp
    @Column(name = "date_creation", updatable = false)
    private LocalDateTime dateCreation;

    @Column(name = "date_collecte")
    private LocalDateTime dateCollecte;

    @Column(name = "date_livraison_prevue")
    private LocalDateTime dateLivraisonPrevue;

    @Column(name = "date_livraison_effective")
    private LocalDateTime dateLivraisonEffective;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_expediteur_id", nullable = false)
    private ClientExpediteur clientExpediteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destinataire_id", nullable = false)
    private Destinataire destinataire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livreur_id")
    private Livreur livreur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ColisProduit> produits = new ArrayList<>();

    @OneToMany(mappedBy = "colis", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HistoriqueLivraison> historiques = new ArrayList<>();
}