# 📋 GUIDE COMPLET DES TESTS API - SMARTLOGI

## 🎯 INFORMATIONS GÉNÉRALES

**Base URL:** `http://localhost:8080`

**Variables d'environnement Apidog:**
- `base_url` = `http://localhost:8080`
- `jwt_token` = (à remplir après le login)

**Credentials:**
| Utilisateur | Username | Password | Rôle | Permissions |
|-------------|----------|----------|------|-------------|
| Manager | `manager` | `manager123` | ROLE_MANAGER | Toutes les permissions |
| Livreur | `livreur` | `livreur123` | ROLE_DELIVERY_PERSON | COLIS_READ, COLIS_UPDATE_STATUS |
| Client | `client` | `client123` | ROLE_CLIENT | COLIS_CREATE, COLIS_READ, PRODUIT_READ |

---

# PHASE 1 : AUTHENTIFICATION (4 tests)

---

## ✅ TEST 1.1 : Login Manager (Succès)

**Objectif:** Authentifier un utilisateur avec le rôle MANAGER et obtenir un token JWT valide.

**Endpoint:** `POST {{base_url}}/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "manager",
  "password": "manager123"
}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJtYW5hZ2VyIiwicm9sZSI6IlJPTEVfTUFOQUdFUiIsInBlcm1pc3Npb25zIjpbIkNPTElTX0NSRUFURSIsIkNPTElTX1JFQUQiLCJDT0xJU19VUERBVEUiLCJDT0xJU19ERUxFVEUiLCJDT0xJU19VUERBVEVfU1RBVFVTIiwiTElWUkVVUl9SRUFEIiwiTElWUkVVUl9NQU5BR0UiLCJaT05FX1JFQUQiLCJaT05FX01BTkFHRSIsIkNMSUVOVF9SRUFEIiwiQ0xJRU5UX01BTkFHRSIsIlBST0RVSVRfUkVBRCIsIlBST0RVSVRfTUFOQUdFIiwiU1RBVFNfVklFVyJdLCJpYXQiOjE3MzU1NTc2MDAsImV4cCI6MTczNTY0NDAwMH0.signature",
  "type": "Bearer",
  "username": "manager",
  "role": "ROLE_MANAGER",
  "expiresIn": 86400
}
```

**Ce que fait ce test:**
1. Envoie les credentials (username + password) au serveur
2. Le serveur vérifie le mot de passe avec BCrypt
3. Si correct, génère un JWT contenant le username, le rôle et les permissions
4. Retourne le JWT avec une durée de validité de 24h (86400 secondes)

**Action après le test:**
⚠️ **IMPORTANT:** Copie la valeur du champ `token` et colle-la dans la variable `{{jwt_token}}` d'Apidog. Ce token sera utilisé pour tous les tests suivants.

**Ce qui se passe côté serveur:**
```
AuthenticationController 
  → AuthenticationService.authenticate()
    → AuthenticationManager vérifie les credentials
    → CustomUserDetailsService charge l'utilisateur + permissions
    → JwtUtil.generateToken() crée le JWT
    → Retourne AuthResponse
```

---

## ✅ TEST 1.2 : Login Livreur (Succès)

**Objectif:** Authentifier un utilisateur avec le rôle DELIVERY_PERSON (livreur).

**Endpoint:** `POST {{base_url}}/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "livreur",
  "password": "livreur123"
}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "livreur",
  "role": "ROLE_DELIVERY_PERSON",
  "expiresIn": 86400
}
```

**Différence avec le Manager:**
Le token du livreur contient **seulement 2 permissions:**
- `COLIS_READ` : Peut consulter les colis
- `COLIS_UPDATE_STATUS` : Peut changer le statut d'un colis (ex: "LIVRE")

**Utilisation:**
Ce test permet de vérifier que le système RBAC fonctionne. Le livreur ne pourra PAS :
- Supprimer des colis (pas de `COLIS_DELETE`)
- Voir la liste des livreurs (pas de `LIVREUR_READ`)
- Créer des produits (pas de `PRODUIT_MANAGE`)

---

## ✅ TEST 1.3 : Login Client (Succès)

**Objectif:** Authentifier un utilisateur avec le rôle CLIENT (client expéditeur).

**Endpoint:** `POST {{base_url}}/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "client",
  "password": "client123"
}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "username": "client",
  "role": "ROLE_CLIENT",
  "expiresIn": 86400
}
```

**Permissions du client:**
- `COLIS_CREATE` : Peut créer de nouveaux colis
- `COLIS_READ` : Peut consulter ses propres colis
- `PRODUIT_READ` : Peut voir le catalogue de produits

**Restrictions:**
Le client ne peut PAS :
- Voir tous les colis (seulement les siens)
- Voir la liste des livreurs
- Supprimer des colis
- Gérer les zones

---

## ❌ TEST 1.4 : Login avec mauvais mot de passe (Échec)

**Objectif:** Vérifier que l'authentification échoue avec un mauvais mot de passe.

**Endpoint:** `POST {{base_url}}/auth/login`

**Headers:**
```
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "username": "manager",
  "password": "wrongpassword"
}
```

**Statut attendu:** `401 Unauthorized`

**Réponse attendue:**
```json
{
  "timestamp": "2024-12-30T10:00:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Bad credentials",
  "path": "/auth/login"
}
```

**Ce que fait ce test:**
1. Envoie un mauvais mot de passe
2. BCrypt compare le hash : `$2a$12$...` ≠ hash("wrongpassword")
3. AuthenticationManager lance une exception `BadCredentialsException`
4. Le serveur retourne 401 sans générer de JWT

**Sécurité:**
- Le message d'erreur ne dit pas si le username existe ou non (sécurité)
- Pas de JWT généré
- L'utilisateur ne peut pas accéder aux ressources protégées

---

# PHASE 2 : COLIS AVEC ROLE MANAGER (9 tests)

**⚠️ PRÉREQUIS:** Login avec `manager/manager123` et copier le token dans `{{jwt_token}}`

---

## ✅ TEST 2.1 : Lister tous les colis

**Objectif:** Récupérer la liste complète de tous les colis du système.

**Endpoint:** `GET {{base_url}}/api/colis`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
[
  {
    "id": 1,
    "numeroSuivi": "COL-12345ABC",
    "statut": "EN_TRANSIT",
    "priorite": "NORMALE",
    "villeDestination": "Casablanca",
    "poidsTotal": 0.2,
    "description": "Commande électronique urgente",
    "dateCreation": "2024-12-20T10:00:00",
    "dateLivraisonPrevue": "2024-12-25T00:00:00"
  },
  {
    "id": 2,
    "numeroSuivi": "COL-67890DEF",
    "statut": "EN_PREPARATION",
    "priorite": "URGENTE",
    "villeDestination": "Casablanca",
    "poidsTotal": 2.5,
    "description": "Matériel informatique",
    "dateCreation": "2024-12-20T11:00:00",
    "dateLivraisonPrevue": "2024-12-22T00:00:00"
  },
  {
    "id": 3,
    "numeroSuivi": "COL-11223GHI",
    "statut": "CREE",
    "priorite": "NORMALE",
    "villeDestination": "Casablanca",
    "poidsTotal": 15.0,
    "description": "Mobilier de bureau",
    "dateCreation": "2024-12-20T12:00:00",
    "dateLivraisonPrevue": "2024-12-28T00:00:00"
  }
]
```

**Ce que fait ce test:**
1. JwtAuthenticationFilter intercepte la requête
2. Extrait et valide le JWT
3. Charge l'utilisateur (manager) avec ses permissions
4. Spring Security vérifie `@PreAuthorize("hasAuthority('COLIS_READ')")`
5. ✅ Manager a COLIS_READ → Autorisé
6. ColisController.getAllColis() s'exécute
7. ColisService.findAll() récupère tous les colis
8. ColisMapper convertit Entity → DTO
9. Retourne la liste en JSON

**Permission requise:** `COLIS_READ`

**Ce test valide:**
- Le JWT est valide
- L'utilisateur est authentifié
- L'utilisateur a la permission COLIS_READ
- Les données de test sont bien insérées dans la BDD

---

## ✅ TEST 2.2 : Récupérer un colis par ID

**Objectif:** Récupérer les détails d'un colis spécifique en utilisant son ID.

**Endpoint:** `GET {{base_url}}/api/colis/1`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "id": 1,
  "numeroSuivi": "COL-12345ABC",
  "statut": "EN_TRANSIT",
  "priorite": "NORMALE",
  "villeDestination": "Casablanca",
  "poidsTotal": 0.2,
  "description": "Commande électronique urgente",
  "dateCreation": "2024-12-20T10:00:00",
  "dateLivraisonPrevue": "2024-12-25T00:00:00",
  "dateCollecte": null,
  "dateLivraisonEffective": null
}
```

**Ce que fait ce test:**
1. Vérifie le JWT (comme 2.1)
2. Vérifie la permission COLIS_READ
3. ColisService.findById(1) récupère le colis ID=1
4. Si le colis n'existe pas → 404 Not Found
5. Si le colis existe → Retourne les détails

**Cas d'erreur:**
- Si ID inexistant (ex: `/api/colis/999`) → `404 Not Found`
- Si pas de JWT → `401 Unauthorized`
- Si JWT invalide → `401 Unauthorized`
- Si pas de permission → `403 Forbidden`

**Permission requise:** `COLIS_READ`

---

## ✅ TEST 2.3 : Récupérer les détails complets d'un colis

**Objectif:** Récupérer toutes les informations du colis avec les relations (client, destinataire, livreur, produits).

**Endpoint:** `GET {{base_url}}/api/colis/1/details`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "id": 1,
  "numeroSuivi": "COL-12345ABC",
  "statut": "EN_TRANSIT",
  "priorite": "NORMALE",
  "villeDestination": "Casablanca",
  "poidsTotal": 0.2,
  "description": "Commande électronique urgente",
  "clientExpediteur": {
    "id": 3,
    "nom": "Tazi",
    "prenom": "Karim",
    "email": "karim.tazi@example.com",
    "telephone": "0645678901",
    "adresse": "12 Rue Mohamed V, Casablanca"
  },
  "destinataire": {
    "id": 3,
    "nom": "Benjelloun",
    "prenom": "Mehdi",
    "telephone": "0678901234",
    "adresse": "23 Rue des Fleurs, Casablanca, 20000"
  },
  "livreur": {
    "id": 4,
    "nom": "Alami",
    "prenom": "Hassan",
    "telephone": "0612345671",
    "vehicule": "MOTO"
  },
  "zone": {
    "id": 6,
    "nom": "Zone Centre",
    "ville": "Casablanca",
    "codePostal": "20000"
  },
  "produits": [
    {
      "id": 4,
      "nom": "Smartphone Samsung Galaxy",
      "prix": 4500.00,
      "poids": 0.200,
      "quantite": 1,
      "prixUnitaire": 4500.00
    }
  ]
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. ColisService.findByIdWithDetails(1)
3. Utilise les jointures JPA pour charger toutes les relations en une seule requête
4. Convertit toutes les entités en DTOs
5. Retourne l'objet complet

**Différence avec TEST 2.2:**
- Test 2.2 retourne SEULEMENT les infos du colis
- Test 2.3 retourne le colis + client + destinataire + livreur + produits

**Utilisation:**
Ce endpoint est idéal pour afficher une page de détails complète d'un colis dans une interface web.

**Permission requise:** `COLIS_READ`

---

## ✅ TEST 2.4 : Rechercher un colis par numéro de suivi

**Objectif:** Permettre à un client de suivre son colis en utilisant le numéro de tracking.

**Endpoint:** `GET {{base_url}}/api/colis/suivi/COL-12345ABC`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "id": 1,
  "numeroSuivi": "COL-12345ABC",
  "statut": "EN_TRANSIT",
  "priorite": "NORMALE",
  "villeDestination": "Casablanca",
  "poidsTotal": 0.2,
  "description": "Commande électronique urgente",
  "dateCreation": "2024-12-20T10:00:00",
  "dateLivraisonPrevue": "2024-12-25T00:00:00"
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. ColisService.findByNumeroSuivi("COL-12345ABC")
3. Requête SQL: `SELECT * FROM colis WHERE numero_suivi = 'COL-12345ABC'`
4. Si trouvé → Retourne le colis
5. Si non trouvé → 404 Not Found

**Cas d'usage:**
- Un client tape son numéro de suivi sur le site web
- Le système affiche où se trouve le colis
- Permet le suivi de livraison sans connaître l'ID

**Permission requise:** `COLIS_READ`

---

## ✅ TEST 2.5 : Filtrer les colis par statut

**Objectif:** Récupérer tous les colis ayant un statut spécifique (ex: EN_TRANSIT).

**Endpoint:** `GET {{base_url}}/api/colis/statut/EN_TRANSIT`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
[
  {
    "id": 1,
    "numeroSuivi": "COL-12345ABC",
    "statut": "EN_TRANSIT",
    "priorite": "NORMALE",
    "villeDestination": "Casablanca",
    "poidsTotal": 0.2
  }
]
```

**Statuts possibles:**
- `CREE` : Colis créé mais pas encore collecté
- `EN_PREPARATION` : En préparation au dépôt
- `EN_TRANSIT` : En cours de livraison
- `LIVRE` : Livré au destinataire
- `RETOURNE` : Retourné à l'expéditeur
- `ANNULE` : Colis annulé

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. ColisService.findByStatut("EN_TRANSIT")
3. Requête SQL: `SELECT * FROM colis WHERE statut = 'EN_TRANSIT'`
4. Retourne la liste filtrée

**Cas d'usage:**
- Dashboard livreur : "Mes colis EN_TRANSIT"
- Dashboard manager : "Tous les colis LIVRE aujourd'hui"
- Statistiques : "Nombre de colis par statut"

**Permission requise:** `COLIS_READ`

---

## ✅ TEST 2.6 : Changer le statut d'un colis

**Objectif:** Mettre à jour le statut d'un colis (ex: de EN_TRANSIT à LIVRE).

**Endpoint:** `PATCH {{base_url}}/api/colis/1/statut?nouveauStatut=LIVRE`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "id": 1,
  "numeroSuivi": "COL-12345ABC",
  "statut": "LIVRE",
  "priorite": "NORMALE",
  "villeDestination": "Casablanca",
  "poidsTotal": 0.2,
  "dateLivraisonEffective": "2024-12-30T15:30:00"
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. ColisService.updateStatut(1, "LIVRE")
3. Charge le colis depuis la BDD
4. Met à jour le statut
5. Si statut = LIVRE → Met à jour `dateLivraisonEffective`
6. Crée une entrée dans `historique_livraison`
7. Sauvegarde en BDD
8. Retourne le colis modifié

**Workflow métier:**
```
CREE → EN_PREPARATION → EN_TRANSIT → LIVRE
                                   → RETOURNE (si échec)
```

**Permission requise:** `COLIS_UPDATE_STATUS`

**Qui peut faire ce test:**
- ✅ Manager (a toutes les permissions)
- ✅ Livreur (a COLIS_UPDATE_STATUS)
- ❌ Client (n'a pas cette permission)

---

## ✅ TEST 2.7 : Assigner un livreur à un colis

**Objectif:** Attribuer un livreur spécifique à un colis.

**Endpoint:** `PATCH {{base_url}}/api/colis/2/assigner-livreur?livreurId=5`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "id": 2,
  "numeroSuivi": "COL-67890DEF",
  "statut": "EN_PREPARATION",
  "priorite": "URGENTE",
  "villeDestination": "Casablanca",
  "livreur": {
    "id": 5,
    "nom": "Benali",
    "prenom": "Fatima",
    "vehicule": "VOITURE"
  }
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. ColisService.assignerLivreur(colisId=2, livreurId=5)
3. Vérifie que le colis existe
4. Vérifie que le livreur existe
5. Vérifie que le livreur est disponible (`actif = true`)
6. Met à jour `colis.livreur_id = 5`
7. Change le statut à `EN_PREPARATION` si besoin
8. Sauvegarde en BDD
9. Retourne le colis avec le livreur assigné

**Règles métier:**
- Un colis ne peut avoir qu'un seul livreur
- Le livreur doit être actif
- Peut réassigner un autre livreur si nécessaire

**Permission requise:** `COLIS_UPDATE`

---

## ✅ TEST 2.8 : Créer un nouveau colis

**Objectif:** Créer un nouveau colis avec un ou plusieurs produits.

**Endpoint:** `POST {{base_url}}/api/colis`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "clientExpediteurId": 3,
  "destinataireId": 3,
  "zoneDestinationId": 6,
  "priorite": "URGENTE",
  "dateLivraisonPrevue": "2024-12-31",
  "description": "Nouveau colis de test",
  "produits": [
    {
      "produitId": 4,
      "quantite": 1
    },
    {
      "produitId": 5,
      "quantite": 2
    }
  ]
}
```

**Statut attendu:** `201 Created`

**Réponse attendue:**
```json
{
  "id": 4,
  "numeroSuivi": "COL-98765XYZ",
  "statut": "CREE",
  "priorite": "URGENTE",
  "villeDestination": "Casablanca",
  "poidsTotal": 5.2,
  "description": "Nouveau colis de test",
  "dateCreation": "2024-12-30T15:45:00",
  "dateLivraisonPrevue": "2024-12-31T00:00:00",
  "clientExpediteur": {...},
  "destinataire": {...},
  "zone": {...},
  "produits": [...]
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. Valide les données (@Valid sur CreateColisRequest)
3. ColisService.create(request)
4. Vérifie que client, destinataire, zone existent
5. Vérifie que tous les produits existent
6. Génère un numéro de suivi unique (COL-XXXXX)
7. Calcule le poids total (somme des poids des produits × quantité)
8. Crée l'entité Colis
9. Crée les relations ColisProduit (table many-to-many)
10. Sauvegarde en BDD
11. Crée une entrée dans historique_livraison
12. Retourne le colis créé avec status 201

**Validations:**
- clientExpediteurId obligatoire
- destinataireId obligatoire
- zoneDestinationId obligatoire
- priorite obligatoire (NORMALE, URGENTE, EXPRESS)
- Au moins 1 produit dans la liste
- Chaque produit doit avoir quantité > 0

**Permission requise:** `COLIS_CREATE`

**Qui peut faire ce test:**
- ✅ Manager
- ✅ Client (peut créer ses propres colis)
- ❌ Livreur

---

## ✅ TEST 2.9 : Supprimer un colis

**Objectif:** Supprimer définitivement un colis de la base de données.

**Endpoint:** `DELETE {{base_url}}/api/colis/3`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `204 No Content`

**Réponse attendue:** (Pas de body, juste le status 204)

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. ColisService.delete(3)
3. Vérifie que le colis existe
4. Supprime les relations ColisProduit (CASCADE)
5. Supprime l'historique de livraison (CASCADE)
6. Supprime le colis
7. Retourne 204 (No Content)

**⚠️ ATTENTION:**
- Suppression définitive (pas de soft delete)
- Les foreign keys en CASCADE suppriment automatiquement :
  - Les entrées dans `colis_produit`
  - Les entrées dans `historique_livraison`
- Ne supprime PAS le client, destinataire ou livreur (constraint ON DELETE)

**Permission requise:** `COLIS_DELETE`

**Qui peut faire ce test:**
- ✅ Manager
- ❌ Livreur (n'a pas COLIS_DELETE)
- ❌ Client (n'a pas COLIS_DELETE)

---

# PHASE 3 : LIVREURS (5 tests)

---

## ✅ TEST 3.1 : Lister tous les livreurs

**Objectif:** Récupérer la liste de tous les livreurs du système.

**Endpoint:** `GET {{base_url}}/api/livreurs`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
[
  {
    "id": 1,
    "nom": "Alami",
    "prenom": "Mohammed",
    "telephone": "0612345678",
    "vehicule": "CAMIONNETTE",
    "actif": true,
    "zoneAssignee": {
      "id": 1,
      "nom": "Casablanca Centre"
    }
  },
  {
    "id": 4,
    "nom": "Alami",
    "prenom": "Hassan",
    "telephone": "0612345671",
    "vehicule": "MOTO",
    "actif": true,
    "zoneAssignee": {
      "id": 6,
      "nom": "Zone Centre"
    }
  },
  {
    "id": 5,
    "nom": "Benali",
    "prenom": "Fatima",
    "telephone": "0623456782",
    "vehicule": "VOITURE",
    "actif": true,
    "zoneAssignee": {
      "id": 7,
      "nom": "Zone Ain Sebaa"
    }
  }
]
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. LivreurService.findAll()
3. Requête SQL: `SELECT * FROM livreur JOIN zone ON livreur.zone_assignee_id = zone.id`
4. Convertit Entity → DTO
5. Retourne la liste

**Permission requise:** `LIVREUR_READ`

**Cas d'usage:**
- Dashboard manager : Voir tous les livreurs
- Assigner un livreur à un colis : Choisir dans la liste
- Statistiques : Nombre de livreurs actifs/inactifs

---

## ✅ TEST 3.2 : Récupérer un livreur par ID

**Objectif:** Récupérer les détails d'un livreur spécifique.

**Endpoint:** `GET {{base_url}}/api/livreurs/4`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "id": 4,
  "nom": "Alami",
  "prenom": "Hassan",
  "telephone": "0612345671",
  "vehicule": "MOTO",
  "actif": true,
  "zoneAssignee": {
    "id": 6,
    "nom": "Zone Centre",
    "ville": "Casablanca",
    "codePostal": "20000"
  }
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. LivreurService.findById(4)
3. Si non trouvé → 404 Not Found
4. Si trouvé → Retourne le livreur

**Permission requise:** `LIVREUR_READ`

---

## ✅ TEST 3.3 : Créer un nouveau livreur

**Objectif:** Ajouter un nouveau livreur au système.

**Endpoint:** `POST {{base_url}}/api/livreurs`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nom": "Nouveau",
  "prenom": "Livreur",
  "telephone": "0699999999",
  "vehicule": "VOITURE",
  "zoneAssigneeId": 6,
  "actif": true
}
```

**Statut attendu:** `201 Created`

**Réponse attendue:**
```json
{
  "id": 7,
  "nom": "Nouveau",
  "prenom": "Livreur",
  "telephone": "0699999999",
  "vehicule": "VOITURE",
  "actif": true,
  "zoneAssignee": {
    "id": 6,
    "nom": "Zone Centre"
  }
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. Valide les données
3. LivreurService.create(request)
4. Vérifie que la zone existe
5. Vérifie que le téléphone est unique
6. Crée l'entité Livreur
7. Sauvegarde en BDD
8. Retourne le livreur créé avec status 201

**Validations:**
- Nom obligatoire
- Prénom obligatoire
- Téléphone obligatoire et unique
- Vehicule obligatoire (MOTO, VOITURE, CAMIONNETTE)
- zoneAssigneeId doit exister

**Permission requise:** `LIVREUR_MANAGE`

---

## ✅ TEST 3.4 : Modifier un livreur

**Objectif:** Mettre à jour les informations d'un livreur existant.

**Endpoint:** `PUT {{base_url}}/api/livreurs/4`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nom": "Alami",
  "prenom": "Hassan Modifié",
  "telephone": "0612345671",
  "vehicule": "CAMIONNETTE",
  "zoneAssigneeId": 6,
  "actif": true
}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
{
  "id": 4,
  "nom": "Alami",
  "prenom": "Hassan Modifié",
  "telephone": "0612345671",
  "vehicule": "CAMIONNETTE",
  "actif": true,
  "zoneAssignee": {
    "id": 6,
    "nom": "Zone Centre"
  }
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. LivreurService.update(4, request)
3. Charge le livreur existant
4. Met à jour les champs
5. Vérifie que le nouveau téléphone est unique
6. Sauvegarde
7. Retourne le livreur modifié

**Permission requise:** `LIVREUR_MANAGE`

**Cas d'usage:**
- Changer la zone assignée d'un livreur
- Changer le véhicule (upgrade MOTO → CAMIONNETTE)
- Désactiver un livreur (actif = false)

---

## ✅ TEST 3.5 : Supprimer un livreur

**Objectif:** Supprimer un livreur du système.

**Endpoint:** `DELETE {{base_url}}/api/livreurs/6`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `204 No Content`

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. LivreurService.delete(6)
3. Vérifie que le livreur existe
4. ⚠️ Vérifie que le livreur n'a pas de colis en cours
5. Supprime le livreur
6. Retourne 204

**⚠️ RÈGLE MÉTIER:**
Si le livreur a des colis en cours (statut ≠ LIVRE ou ANNULE), la suppression échoue avec une erreur `400 Bad Request` : "Impossible de supprimer ce livreur car il a des colis en cours"

**Permission requise:** `LIVREUR_MANAGE`

---

# PHASE 4 : ZONES (2 tests)

---

## ✅ TEST 4.1 : Lister toutes les zones

**Objectif:** Récupérer la liste de toutes les zones de livraison.

**Endpoint:** `GET {{base_url}}/api/zones`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
[
  {
    "id": 1,
    "nom": "Casablanca Centre",
    "codePostal": "20000",
    "ville": "Casablanca"
  },
  {
    "id": 2,
    "nom": "Rabat Agdal",
    "codePostal": "10000",
    "ville": "Rabat"
  },
  {
    "id": 6,
    "nom": "Zone Centre",
    "codePostal": "20000",
    "ville": "Casablanca"
  },
  {
    "id": 7,
    "nom": "Zone Ain Sebaa",
    "codePostal": "20250",
    "ville": "Casablanca"
  },
  {
    "id": 8,
    "nom": "Zone Maarif",
    "codePostal": "20100",
    "ville": "Casablanca"
  }
]
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. ZoneService.findAll()
3. Retourne la liste de toutes les zones

**Permission requise:** `ZONE_READ`

**Cas d'usage:**
- Dropdown pour créer un colis : Choisir la zone de destination
- Dropdown pour créer un livreur : Assigner une zone
- Carte géographique : Afficher toutes les zones

---

## ✅ TEST 4.2 : Créer une nouvelle zone

**Objectif:** Ajouter une nouvelle zone de livraison.

**Endpoint:** `POST {{base_url}}/api/zones`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nom": "Zone Test",
  "ville": "Casablanca",
  "codePostal": "20500"
}
```

**Statut attendu:** `201 Created`

**Réponse attendue:**
```json
{
  "id": 9,
  "nom": "Zone Test",
  "ville": "Casablanca",
  "codePostal": "20500",
  "createdAt": "2024-12-30T16:00:00"
}
```

**Ce que fait ce test:**
1. Vérifie JWT + permissions
2. Valide les données
3. ZoneService.create(request)
4. Vérifie que le nom est unique
5. Crée l'entité Zone
6. Sauvegarde en BDD
7. Retourne la zone créée avec status 201

**Validations:**
- Nom obligatoire et unique
- Ville obligatoire
- Code postal obligatoire

**Permission requise:** `ZONE_MANAGE`

---

# PHASE 5 : PRODUITS, CLIENTS, DESTINATAIRES (6 tests)

---

## ✅ TEST 5.1 : Lister tous les produits

**Objectif:** Récupérer le catalogue de produits.

**Endpoint:** `GET {{base_url}}/api/produits`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
[
  {
    "id": 1,
    "nom": "Ordinateur Portable Dell",
    "categorie": "Électronique",
    "poids": 2.5,
    "prix": 8500.00
  },
  {
    "id": 4,
    "nom": "Smartphone Samsung Galaxy",
    "categorie": "Électronique",
    "poids": 0.2,
    "prix": 4500.00
  },
  {
    "id": 5,
    "nom": "Laptop Dell XPS",
    "categorie": "Électronique",
    "poids": 2.5,
    "prix": 12000.00
  }
]
```

**Permission requise:** `PRODUIT_READ`

---

## ✅ TEST 5.2 : Créer un nouveau produit

**Objectif:** Ajouter un produit au catalogue.

**Endpoint:** `POST {{base_url}}/api/produits`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nom": "Nouveau Produit",
  "categorie": "Test",
  "poids": 1.5,
  "prix": 250.00
}
```

**Statut attendu:** `201 Created`

**Permission requise:** `PRODUIT_MANAGE`

---

## ✅ TEST 5.3 : Lister tous les clients expéditeurs

**Objectif:** Récupérer la liste de tous les clients qui envoient des colis.

**Endpoint:** `GET {{base_url}}/api/clients`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
[
  {
    "id": 1,
    "nom": "Entreprise Tech",
    "prenom": "Directeur",
    "email": "contact@tech.ma",
    "telephone": "0522123456",
    "adresse": "Boulevard Zerktouni, Casablanca"
  },
  {
    "id": 3,
    "nom": "Tazi",
    "prenom": "Karim",
    "email": "karim.tazi@example.com",
    "telephone": "0645678901",
    "adresse": "12 Rue Mohamed V, Casablanca"
  }
]
```

**Permission requise:** `CLIENT_READ`

---

## ✅ TEST 5.4 : Créer un nouveau client expéditeur

**Objectif:** Enregistrer un nouveau client dans le système.

**Endpoint:** `POST {{base_url}}/api/clients`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nom": "Nouveau",
  "prenom": "Client",
  "email": "nouveau.client@example.com",
  "telephone": "0611111111",
  "adresse": "123 Rue Test, Casablanca"
}
```

**Statut attendu:** `201 Created`

**Permission requise:** `CLIENT_MANAGE`

**Validations:**
- Email obligatoire et unique
- Téléphone obligatoire
- Nom et prénom obligatoires

---

## ✅ TEST 5.5 : Lister tous les destinataires

**Objectif:** Récupérer la liste de tous les destinataires enregistrés.

**Endpoint:** `GET {{base_url}}/api/destinataires`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Permission requise:** `CLIENT_READ`

---

## ✅ TEST 5.6 : Créer un nouveau destinataire

**Objectif:** Enregistrer un nouveau destinataire.

**Endpoint:** `POST {{base_url}}/api/destinataires`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "nom": "Nouveau",
  "prenom": "Destinataire",
  "telephone": "0622222222",
  "adresse": "456 Avenue Test, Casablanca, 20000"
}
```

**Statut attendu:** `201 Created`

**Permission requise:** `CLIENT_MANAGE`

---

# PHASE 6 : ADMINISTRATION DES PERMISSIONS (6 tests)

**⚠️ ATTENTION:** Ces endpoints sont accessibles UNIQUEMENT aux utilisateurs avec le rôle `ROLE_MANAGER`.

---

## ✅ TEST 6.1 : Lister toutes les permissions

**Objectif:** Récupérer la liste de toutes les permissions du système.

**Endpoint:** `GET {{base_url}}/api/admin/permissions`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
[
  {
    "id": 1,
    "name": "COLIS_CREATE",
    "description": "Créer un colis",
    "createdAt": "2024-12-20T10:00:00"
  },
  {
    "id": 2,
    "name": "COLIS_READ",
    "description": "Consulter les colis",
    "createdAt": "2024-12-20T10:00:00"
  },
  {
    "id": 3,
    "name": "COLIS_UPDATE",
    "description": "Modifier un colis",
    "createdAt": "2024-12-20T10:00:00"
  },
  {
    "id": 4,
    "name": "COLIS_DELETE",
    "description": "Supprimer un colis",
    "createdAt": "2024-12-20T10:00:00"
  }
]
```

**Rôle requis:** `ROLE_MANAGER`

**Ce test valide:**
- Seul le Manager peut accéder à l'administration
- Livreur et Client reçoivent 403 Forbidden

---

## ✅ TEST 6.2 : Créer une nouvelle permission

**Objectif:** Ajouter une nouvelle permission au système.

**Endpoint:** `POST {{base_url}}/api/admin/permissions`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "TEST_PERMISSION",
  "description": "Permission de test"
}
```

**Statut attendu:** `201 Created`

**Réponse attendue:**
```json
{
  "id": 15,
  "name": "TEST_PERMISSION",
  "description": "Permission de test",
  "createdAt": "2024-12-30T16:15:00"
}
```

**Rôle requis:** `ROLE_MANAGER`

---

## ✅ TEST 6.3 : Modifier une permission

**Objectif:** Mettre à jour une permission existante.

**Endpoint:** `PUT {{base_url}}/api/admin/permissions/15`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "TEST_PERMISSION_UPDATED",
  "description": "Permission modifiée"
}
```

**Statut attendu:** `200 OK`

**Rôle requis:** `ROLE_MANAGER`

---

## ✅ TEST 6.4 : Supprimer une permission

**Objectif:** Supprimer une permission du système.

**Endpoint:** `DELETE {{base_url}}/api/admin/permissions/15`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `204 No Content`

**⚠️ ATTENTION:**
- Si la permission est utilisée par des rôles, la suppression échoue (foreign key constraint)
- Il faut d'abord retirer la permission de tous les rôles

**Rôle requis:** `ROLE_MANAGER`

---

## ✅ TEST 6.5 : Lister tous les rôles

**Objectif:** Récupérer la liste de tous les rôles avec leurs permissions.

**Endpoint:** `GET {{base_url}}/api/admin/roles`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
```

**Statut attendu:** `200 OK`

**Réponse attendue:**
```json
[
  {
    "id": 1,
    "name": "ROLE_MANAGER",
    "description": "Gestionnaire avec accès complet",
    "permissions": [
      {"id": 1, "name": "COLIS_CREATE"},
      {"id": 2, "name": "COLIS_READ"},
      {"id": 3, "name": "COLIS_UPDATE"},
      {"id": 4, "name": "COLIS_DELETE"},
      {"id": 5, "name": "COLIS_UPDATE_STATUS"},
      {"id": 6, "name": "LIVREUR_READ"},
      {"id": 7, "name": "LIVREUR_MANAGE"}
    ]
  },
  {
    "id": 2,
    "name": "ROLE_DELIVERY_PERSON",
    "description": "Livreur avec accès limité",
    "permissions": [
      {"id": 2, "name": "COLIS_READ"},
      {"id": 5, "name": "COLIS_UPDATE_STATUS"}
    ]
  },
  {
    "id": 3,
    "name": "ROLE_CLIENT",
    "description": "Client expéditeur",
    "permissions": [
      {"id": 1, "name": "COLIS_CREATE"},
      {"id": 2, "name": "COLIS_READ"},
      {"id": 11, "name": "PRODUIT_READ"}
    ]
  }
]
```

**Rôle requis:** `ROLE_MANAGER`

---

## ✅ TEST 6.6 : Créer un nouveau rôle

**Objectif:** Ajouter un nouveau rôle au système.

**Endpoint:** `POST {{base_url}}/api/admin/roles`

**Headers:**
```
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

**Body (JSON):**
```json
{
  "name": "ROLE_TEST",
  "description": "Rôle de test",
  "permissionIds": [2, 11]
}
```

**Statut attendu:** `201 Created`

**Réponse attendue:**
```json
{
  "id": 4,
  "name": "ROLE_TEST",
  "description": "Rôle de test",
  "permissions": [
    {"id": 2, "name": "COLIS_READ"},
    {"id": 11, "name": "PRODUIT_READ"}
  ]
}
```

**Ce que fait ce test:**
1. Crée un nouveau rôle
2. Associe les permissions via la table `role_permissions`
3. Retourne le rôle avec ses permissions

**Rôle requis:** `ROLE_MANAGER`

---

# PHASE 7 : TESTS RBAC ET SÉCURITÉ (6 tests)

**Objectif:** Valider que le système de permissions fonctionne correctement.

---

## ❌ TEST 7.1 : Livreur ne voit QUE ses colis

**Objectif:** Vérifier que le livreur ne peut voir que les colis qui lui sont assignés.

**Étapes:**
1. Login avec `livreur/livreur123`
2. Copier le token dans une variable `{{jwt_token_livreur}}`
3. `GET {{base_url}}/api/colis`

**Headers:**
```
Authorization: Bearer {{jwt_token_livreur}}
```

**Statut attendu:** `200 OK`

**Résultat attendu:**
Le livreur ne devrait voir QUE les colis où `livreur_id` correspond à son ID.

**Ce test valide:**
- Le filtrage des données par rôle fonctionne
- Le livreur n'a pas accès à tous les colis
- La sécurité au niveau des données (Row-Level Security)

---

## ❌ TEST 7.2 : Livreur ne peut PAS supprimer de colis

**Objectif:** Vérifier que le livreur n'a pas la permission COLIS_DELETE.

**Étapes:**
1. Login avec `livreur/livreur123`
2. `DELETE {{base_url}}/api/colis/1`

**Headers:**
```
Authorization: Bearer {{jwt_token_livreur}}
```

**Statut attendu:** `403 Forbidden`

**Réponse attendue:**
```json
{
  "timestamp": "2024-12-30T16:30:00",
  "status": 403,
  "error": "Forbidden",
  "message": "Access Denied",
  "path": "/api/colis/1"
}
```

**Ce test valide:**
- `@PreAuthorize("hasAuthority('COLIS_DELETE')")` bloque l'accès
- Le livreur reçoit 403 car il n'a pas cette permission

---

## ❌ TEST 7.3 : Client ne peut PAS voir la liste des livreurs

**Objectif:** Vérifier que le client n'a pas accès aux informations des livreurs.

**Étapes:**
1. Login avec `client/client123`
2. `GET {{base_url}}/api/livreurs`

**Headers:**
```
Authorization: Bearer {{jwt_token_client}}
```

**Statut attendu:** `403 Forbidden`

**Ce test valide:**
- `@PreAuthorize("hasAuthority('LIVREUR_READ')")` bloque l'accès
- Le client n'a pas la permission LIVREUR_READ

---

## ✅ TEST 7.4 : Client peut voir ses propres colis

**Objectif:** Vérifier que le client peut consulter ses colis.

**Étapes:**
1. Login avec `client/client123`
2. `GET {{base_url}}/api/colis`

**Headers:**
```
Authorization: Bearer {{jwt_token_client}}
```

**Statut attendu:** `200 OK`

**Résultat attendu:**
Le client devrait voir SEULEMENT les colis où `client_expediteur_id` correspond à son ID.

---

## ❌ TEST 7.5 : Accès sans token (401)

**Objectif:** Vérifier qu'on ne peut pas accéder à l'API sans JWT.

**Endpoint:** `GET {{base_url}}/api/colis`

**Headers:** (AUCUN header Authorization)

**Statut attendu:** `401 Unauthorized`

**Réponse attendue:**
```json
{
  "timestamp": "2024-12-30T16:35:00",
  "status": 401,
  "error": "Unauthorized",
  "message": "Full authentication is required to access this resource",
  "path": "/api/colis"
}
```

**Ce qui se passe:**
1. JwtAuthenticationFilter ne trouve pas de header Authorization
2. SecurityContextHolder ne contient pas d'utilisateur authentifié
3. Spring Security bloque l'accès
4. JwtAuthenticationEntryPoint retourne 401

---

## ❌ TEST 7.6 : Token invalide (401)

**Objectif:** Vérifier qu'un token invalide est rejeté.

**Endpoint:** `GET {{base_url}}/api/colis`

**Headers:**
```
Authorization: Bearer INVALID_TOKEN_12345
```

**Statut attendu:** `401 Unauthorized`

**Ce qui se passe:**
1. JwtAuthenticationFilter extrait le token
2. JwtUtil.validateToken() échoue (signature invalide)
3. Aucun utilisateur n'est mis dans SecurityContext
4. Spring Security bloque l'accès
5. Retourne 401

**Ce test valide:**
- La signature JWT est vérifiée
- Les tokens forgés sont rejetés
- La sécurité de l'API est solide

---

# 📊 RÉCAPITULATIF DES PERMISSIONS

| Permission | Manager | Livreur | Client |
|-----------|---------|---------|--------|
| COLIS_CREATE | ✅ | ❌ | ✅ |
| COLIS_READ | ✅ | ✅ (ses colis) | ✅ (ses colis) |
| COLIS_UPDATE | ✅ | ❌ | ❌ |
| COLIS_DELETE | ✅ | ❌ | ❌ |
| COLIS_UPDATE_STATUS | ✅ | ✅ | ❌ |
| LIVREUR_READ | ✅ | ❌ | ❌ |
| LIVREUR_MANAGE | ✅ | ❌ | ❌ |
| ZONE_READ | ✅ | ❌ | ❌ |
| ZONE_MANAGE | ✅ | ❌ | ❌ |
| CLIENT_READ | ✅ | ❌ | ❌ |
| CLIENT_MANAGE | ✅ | ❌ | ❌ |
| PRODUIT_READ | ✅ | ❌ | ✅ |
| PRODUIT_MANAGE | ✅ | ❌ | ❌ |
| STATS_VIEW | ✅ | ❌ | ❌ |

---

# 🚀 ORDRE D'EXÉCUTION RECOMMANDÉ

1. **Phase 1** (Tests 1.1 à 1.4) : Authentification
2. **Phase 2** (Tests 2.1 à 2.9) : CRUD Colis avec Manager
3. **Phase 3** (Tests 3.1 à 3.5) : CRUD Livreurs
4. **Phase 4** (Tests 4.1 à 4.2) : CRUD Zones
5. **Phase 5** (Tests 5.1 à 5.6) : CRUD Produits/Clients/Destinataires
6. **Phase 6** (Tests 6.1 à 6.6) : Administration Permissions
7. **Phase 7** (Tests 7.1 à 7.6) : Validation RBAC et Sécurité

---

# 📝 NOTES IMPORTANTES

- **Toujours copier le token** après le login dans `{{jwt_token}}`
- **Les tokens expirent après 24h** (86400 secondes)
- **Les tests 7.X nécessitent des tokens différents** (manager, livreur, client)
- **Certains tests modifient la BDD** (créations, modifications, suppressions)
- **L'ordre d'exécution peut affecter les résultats** (ex: le test 2.9 supprime le colis 3)

---

**Fin du guide complet des tests API SmartLogi** 🎉
