# 📦 IntelliDrop V2 - Smart Delivery Management System

**Version:** 1.0.0  
**Framework:** Spring Boot 3.3.0  
**Java:** 21  
**Database:** PostgreSQL

---

## 📋 Table des matières

- [Description](#-description)
- [Fonctionnalités](#-fonctionnalités)
- [Technologies utilisées](#-technologies-utilisées)
- [Architecture](#-architecture)
- [Documentation API](#-documentation-api)
- [Endpoints disponibles](#-endpoints-disponibles)
- [Modèle de données](#-modèle-de-données)

---

## 🎯 Description

**IntelliDrop V2** est une application web de gestion intelligente de livraisons pour **SmartLogi**, une entreprise de livraison de colis opérant au Maroc.

Le système remplace la gestion manuelle Excel par une solution automatisée complète permettant de :
- Gérer les clients expéditeurs et destinataires
- Suivre les colis en temps réel
- Assigner des livreurs aux colis
- Gérer les zones de livraison
- Historiser tous les changements de statut
- Gérer le catalogue de produits

---

## ✨ Fonctionnalités

### 👥 Gestion des utilisateurs
- **Clients expéditeurs** : Entreprises et particuliers envoyant des colis
- **Destinataires** : Personnes recevant les colis
- **Livreurs** : Gestion des livreurs avec affectation par zone

### 📦 Gestion des colis
- Création de colis avec multiple produits
- Génération automatique de numéros de suivi (format: COL-XXXXXXXX)
- Gestion des priorités (Normale, Urgente, Très urgente)
- Suivi des statuts (Créé, Collecté, En stock, En transit, Livré)
- Calcul automatique du poids et prix total
- Historique complet des changements de statut

### 🗺️ Gestion logistique
- Zones de livraison par ville et code postal
- Assignment automatique des livreurs
- Suivi des véhicules (Voiture, Camionnette, Moto)
- Calcul des charges par livreur et par zone

### 📊 Reporting
- Colis en retard
- Colis non assignés
- Statistiques par zone, livreur, statut
- Historique détaillé des livraisons

---

## 🛠 Technologies utilisées

### Backend
- **Java 21** - Langage de programmation
- **Spring Boot 3.3.0** - Framework principal
    - Spring Data JPA - Persistance des données
    - Spring Web - API REST
    - Spring Validation - Validation des données
    - Spring Mail - Envoi d'emails (bonus)

### Base de données
- **PostgreSQL 15+** - Base de données relationnelle
- **Liquibase** - Gestion des migrations de schéma

### Outils de développement
- **Lombok** - Réduction du code boilerplate
- **MapStruct 1.5.5** - Mapping Entity ↔ DTO
- **SpringDoc OpenAPI 2.3.0** - Documentation API (Swagger)
- **Maven** - Gestion des dépendances

### Tests (à venir - Brief 2)
- **JUnit 5** - Tests unitaires
- **Mockito** - Mocking
- **Spring Boot Test** - Tests d'intégration
- **H2** - Base de données en mémoire pour tests

---

## 🏗 Architecture

### Structure du projet

```
smartlogi/
├── src/main/java/org/example/smartlogi/
│   ├── controller/          # Contrôleurs REST
│   │   ├── ClientExpediteurController.java
│   │   ├── DestinataireController.java
│   │   ├── ZoneController.java
│   │   ├── LivreurController.java
│   │   ├── ProduitController.java
│   │   └── ColisController.java
│   │
│   ├── service/             # Logique métier
│   │   ├── ClientExpediteurService.java
│   │   ├── DestinataireService.java
│   │   ├── ZoneService.java
│   │   ├── LivreurService.java
│   │   ├── ProduitService.java
│   │   └── ColisService.java
│   │
│   ├── repository/          # Accès aux données
│   │   ├── ClientExpediteurRepository.java
│   │   ├── DestinataireRepository.java
│   │   ├── ZoneRepository.java
│   │   ├── LivreurRepository.java
│   │   ├── ProduitRepository.java
│   │   ├── ColisRepository.java
│   │   ├── ColisProduitRepository.java
│   │   └── HistoriqueLivraisonRepository.java
│   │
│   ├── entity/              # Entités JPA
│   │   ├── ClientExpediteur.java
│   │   ├── Destinataire.java
│   │   ├── Zone.java
│   │   ├── Livreur.java
│   │   ├── Produit.java
│   │   ├── Colis.java
│   │   ├── ColisProduit.java
│   │   └── HistoriqueLivraison.java
│   │
│   ├── dto/                 # Data Transfer Objects
│   │   ├── ClientExpediteurDTO.java
│   │   ├── DestinataireDTO.java
│   │   ├── ZoneDTO.java
│   │   ├── LivreurDTO.java
│   │   ├── ProduitDTO.java
│   │   ├── ColisDTO.java
│   │   ├── ColisDetailDTO.java
│   │   ├── CreateColisRequest.java
│   │   └── ...
│   │
│   ├── mapper/              # MapStruct mappers
│   │   ├── ClientExpediteurMapper.java
│   │   ├── DestinataireMapper.java
│   │   ├── ZoneMapper.java
│   │   ├── LivreurMapper.java
│   │   ├── ProduitMapper.java
│   │   └── ColisMapper.java
│   │
│   ├── enums/               # Énumérations
│   │   ├── StatutColis.java
│   │   ├── PrioriteColis.java
│   │   └── TypeVehicule.java
│   │
│   └── SmartlogiApplication.java  # Classe principale
│
├── src/main/resources/
│   ├── application.properties      # Configuration
│   └── db/changelog/               # Migrations Liquibase
│       ├── db.changelog-master.yaml
│       ├── 001-create-tables.yaml
│       ├── 002-add-constraints.yaml
│       ├── 003-insert-initial-data.yaml
│       └── 004-add-indexes.yaml
│
└── pom.xml                         # Configuration Maven
```

### Pattern architectural

**Architecture en couches (Layered Architecture)**

```
┌─────────────────────────────────────┐
│   Controller Layer (REST API)      │  ← Exposition HTTP
├─────────────────────────────────────┤
│   Service Layer (Business Logic)   │  ← Logique métier
├─────────────────────────────────────┤
│   Repository Layer (Data Access)   │  ← Accès données
├─────────────────────────────────────┤
│   Entity Layer (Domain Model)      │  ← Modèle de domaine
└─────────────────────────────────────┘
          ↓
    PostgreSQL Database
```

---

## 📖 Documentation API

### Swagger UI

Documentation interactive disponible à :

🔗 **http://localhost:8080/swagger-ui/index.html**

### OpenAPI JSON

Schéma OpenAPI disponible à :

🔗 **http://localhost:8080/v3/api-docs**

---

## 🔗 Endpoints disponibles

### 📊 Statistiques globales

| Module | Endpoints |
|--------|-----------|
| **Zones** | 10 endpoints |
| **Clients Expéditeurs** | 10 endpoints |
| **Destinataires** | 10 endpoints |
| **Livreurs** | 16 endpoints |
| **Produits** | 9 endpoints |
| **Colis** | 16 endpoints |
| **TOTAL** | **71 endpoints** |

---

### 🗺️ Zones

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/zones` | Liste toutes les zones |
| GET | `/api/zones/{id}` | Détails d'une zone |
| POST | `/api/zones` | Créer une zone |
| PUT | `/api/zones/{id}` | Modifier une zone |
| DELETE | `/api/zones/{id}` | Supprimer une zone |
| GET | `/api/zones/ville/{ville}` | Zones par ville |
| GET | `/api/zones/search?keyword={mot}` | Recherche |

---

### 👤 Clients Expéditeurs

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/clients` | Liste tous les clients |
| GET | `/api/clients/{id}` | Détails d'un client |
| POST | `/api/clients` | Créer un client |
| PUT | `/api/clients/{id}` | Modifier un client |
| DELETE | `/api/clients/{id}` | Supprimer un client |
| GET | `/api/clients/email/{email}` | Client par email |
| GET | `/api/clients/search?keyword={mot}` | Recherche |

---

### 📮 Destinataires

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/destinataires` | Liste tous les destinataires |
| GET | `/api/destinataires/{id}` | Détails d'un destinataire |
| POST | `/api/destinataires` | Créer un destinataire |
| PUT | `/api/destinataires/{id}` | Modifier un destinataire |
| DELETE | `/api/destinataires/{id}` | Supprimer un destinataire |
| GET | `/api/destinataires/telephone/{tel}` | Par téléphone |
| GET | `/api/destinataires/search?keyword={mot}` | Recherche |

---

### 🚚 Livreurs

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/livreurs` | Liste tous les livreurs |
| GET | `/api/livreurs/{id}` | Détails d'un livreur |
| POST | `/api/livreurs` | Créer un livreur |
| PUT | `/api/livreurs/{id}` | Modifier un livreur |
| DELETE | `/api/livreurs/{id}` | Supprimer un livreur |
| GET | `/api/livreurs/actifs` | Livreurs actifs |
| GET | `/api/livreurs/zone/{zoneId}` | Par zone |
| GET | `/api/livreurs/vehicule/{type}` | Par véhicule |
| PATCH | `/api/livreurs/{id}/activer` | Activer |
| PATCH | `/api/livreurs/{id}/desactiver` | Désactiver |
| PATCH | `/api/livreurs/{id}/zone/{zoneId}` | Changer zone |

---

### 📦 Produits

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/produits` | Liste tous les produits |
| GET | `/api/produits/{id}` | Détails d'un produit |
| POST | `/api/produits` | Créer un produit |
| PUT | `/api/produits/{id}` | Modifier un produit |
| DELETE | `/api/produits/{id}` | Supprimer un produit |
| GET | `/api/produits/categorie/{cat}` | Par catégorie |
| GET | `/api/produits/search?keyword={mot}` | Recherche |

---

### 📦 Colis (Module principal)

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/colis` | Liste tous les colis |
| GET | `/api/colis/{id}` | Détails d'un colis |
| GET | `/api/colis/{id}/details` | Détails complets |
| POST | `/api/colis` | Créer un colis |
| DELETE | `/api/colis/{id}` | Supprimer un colis |
| GET | `/api/colis/numero-suivi/{numero}` | Par numéro de suivi |
| GET | `/api/colis/statut/{statut}` | Par statut |
| GET | `/api/colis/client/{clientId}` | Colis d'un client |
| GET | `/api/colis/livreur/{livreurId}` | Colis d'un livreur |
| GET | `/api/colis/zone/{zoneId}` | Colis d'une zone |
| GET | `/api/colis/en-retard` | Colis en retard |
| GET | `/api/colis/{id}/historique` | Historique complet |
| PATCH | `/api/colis/{id}/statut` | Changer le statut |
| PATCH | `/api/colis/{colisId}/assigner-livreur/{livreurId}` | Assigner livreur |

---

## 🗄️ Modèle de données

### Schéma relationnel

```
┌─────────────────┐         ┌──────────────────┐
│  Zone           │◄───┐    │  ClientExpediteur│
│  - id           │    │    │  - id            │
│  - nom          │    │    │  - nom           │
│  - code_postal  │    │    │  - prenom        │
│  - ville        │    │    │  - email         │
└─────────────────┘    │    │  - telephone     │
                       │    │  - adresse       │
┌─────────────────┐    │    └──────────────────┘
│  Livreur        │    │           │
│  - id           │    │           │ 1
│  - nom          │    │           │
│  - prenom       │────┘           │
│  - telephone    │                │
│  - vehicule     │                │
│  - zone_id      │                │ *
│  - actif        │    ┌───────────▼──────────┐
└─────────────────┘    │  Colis               │
         │             │  - id                │
         │ *           │  - numero_suivi      │
         │             │  - poids_total       │
         │             │  - statut            │
         └─────────────┤  - priorite          │
                       │  - client_id         │
                       │  - destinataire_id   │
                       │  - livreur_id        │
                       │  - zone_id           │
                       └──────────────────────┘
                              │
                    ┌─────────┴─────────┐
                    │ *               * │
         ┌──────────▼────────┐  ┌──────▼──────────────┐
         │ ColisProduit      │  │ HistoriqueLivraison │
         │ - colis_id        │  │ - id                │
         │ - produit_id      │  │ - colis_id          │
         │ - quantite        │  │ - statut            │
         │ - prix_unitaire   │  │ - date_changement   │
         └───────────────────┘  │ - commentaire       │
                │               └─────────────────────┘
                │
         ┌──────▼──────────┐
         │  Produit        │
         │  - id           │
         │  - nom          │
         │  - categorie    │
         │  - poids        │
         │  - prix         │
         └─────────────────┘
```

### Énumérations

#### StatutColis
- `CREE` - Créé
- `COLLECTE` - Collecté
- `EN_STOCK` - En stock
- `EN_TRANSIT` - En transit
- `LIVRE` - Livré

#### PrioriteColis
- `NORMALE` - Normale
- `URGENTE` - Urgente
- `TRES_URGENTE` - Très urgente

#### TypeVehicule
- `VOITURE` - Voiture
- `CAMIONNETTE` - Camionnette
- `MOTO` - Moto

---

**⭐ Si ce projet vous plaît, n'hésitez pas à lui donner une étoile !**