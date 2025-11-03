package org.example.smartlogi.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "livreur")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Livreur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false, length = 100)
    private String nom;

    @Column(name = "prenom", nullable = false, length = 100)
    private String prenom;

    @Column(name = "telephone", nullable = false, unique = true, length = 20)
    private String telephone;

    @Column(name = "vehicule", nullable = false, length = 50)
    private String vehicule;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_assignee_id")
    private Zone zoneAssignee;

    @Column(name = "actif")
    private Boolean actif = true;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}