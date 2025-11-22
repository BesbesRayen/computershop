# 📋 SCHÉMA FIRESTORE - COLLECTIONS & CHAMPS

## Collection 1: Internaute (Utilisateurs)

### Document Structure
```
Collection: Internaute
Document ID: [Auto-généré par Firebase Auth - UID]
```

### Champs & Types

| Champ | Type | Description | Exemple |
|-------|------|-------------|---------|
| `idInt` | String | ID unique de l'utilisateur (UID Firebase) | `"abc123def456ghi789"` |
| `login` | String | Nom d'utilisateur/pseudo | `"rayen"` |
| `email` | String | Adresse email unique | `"rayen@example.com"` |
| `dateInscrip` | Number (Long) | Date d'inscription en millisecondes | `1700644800000` |
| `pays` | String | Pays de l'utilisateur | `"Tunisia"` |
| `role` | String | Rôle utilisateur: "user" ou "admin" | `"user"` |

### Exemple Complet d'un Document

```json
{
  "idInt": "abc123def456ghi789",
  "login": "rayen",
  "email": "rayen@example.com",
  "dateInscrip": 1700644800000,
  "pays": "Tunisia",
  "role": "user"
}
```

### Exemple Admin

```json
{
  "idInt": "xyz789abc456def123",
  "login": "admin_user",
  "email": "admin@example.com",
  "dateInscrip": 1700544800000,
  "pays": "Tunisia",
  "role": "admin"
}
```

---

## Collection 2: Article (Produits)

### Document Structure
```
Collection: Article
Document ID: [Auto-généré - ID produit]
```

### Champs & Types

| Champ | Type | Description | Exemple |
|-------|------|-------------|---------|
| `idArt` | String | ID unique du produit | `"prod_001_laptop"` |
| `libArt` | String | Nom/libellé du produit | `"Dell XPS 13 Laptop"` |
| `prixArt` | Number (Double) | Prix du produit en dinars | `1299.99` |
| `catArt` | String | Catégorie du produit | `"Laptops"` |
| `description` | String | Description détaillée | `"Intel i7, 16GB RAM, 512GB SSD"` |
| `stock` | Number (Integer) | Quantité en stock | `15` |
| `imageUrl` | String | URL de l'image du produit | `"https://example.com/images/dell-xps.jpg"` |

### Exemple Complet d'un Document

```json
{
  "idArt": "prod_001_laptop",
  "libArt": "Dell XPS 13 Laptop",
  "prixArt": 1299.99,
  "catArt": "Laptops",
  "description": "Intel i7, 16GB RAM, 512GB SSD, 13.4 inch FHD Display",
  "stock": 15,
  "imageUrl": "https://example.com/images/dell-xps.jpg"
}
```

### Autres Exemples

#### Produit 2: Souris
```json
{
  "idArt": "prod_002_mouse",
  "libArt": "Logitech MX Master 3S",
  "prixArt": 99.99,
  "catArt": "Peripherals",
  "description": "Wireless mouse with precision scrolling",
  "stock": 50,
  "imageUrl": "https://example.com/images/logitech-mouse.jpg"
}
```

#### Produit 3: Clavier
```json
{
  "idArt": "prod_003_keyboard",
  "libArt": "Corsair K95 Platinum",
  "prixArt": 199.99,
  "catArt": "Peripherals",
  "description": "Mechanical gaming keyboard with RGB lighting",
  "stock": 30,
  "imageUrl": "https://example.com/images/corsair-keyboard.jpg"
}
```

#### Produit 4: Écran
```json
{
  "idArt": "prod_004_monitor",
  "libArt": "LG UltraWide 34 inch",
  "prixArt": 799.99,
  "catArt": "Monitors",
  "description": "3440x1440 resolution, 144Hz refresh rate",
  "stock": 8,
  "imageUrl": "https://example.com/images/lg-monitor.jpg"
}
```

---

## Collection 3: Panier (Panier d'Achat)

### Document Structure
```
Collection: Panier
Document ID: [Auto-généré - ID du panier]
```

### Champs & Types

| Champ | Type | Description | Exemple |
|-------|------|-------------|---------|
| `numPanier` | String | ID unique du panier | `"cart_abc123def456"` |
| `idArt` | String | Référence à l'ID du produit | `"prod_001_laptop"` |
| `idInt` | String | Référence à l'ID de l'utilisateur | `"abc123def456ghi789"` |
| `quantité` | Number (Integer) | Quantité du produit dans le panier | `2` |
| `emballage` | String | Type d'emballage | `"Gift Wrap"` |
| `dateAjout` | Number (Long) | Date d'ajout au panier en millisecondes | `1700644800000` |

### Options d'Emballage (emballage)
```
- "Standard"      (Standard)
- "Gift Wrap"     (Emballage Cadeau)
- "Express"       (Express)
- "Fragile"       (Fragile)
```

### Exemple Complet d'un Document

```json
{
  "numPanier": "cart_abc123def456",
  "idArt": "prod_001_laptop",
  "idInt": "abc123def456ghi789",
  "quantité": 1,
  "emballage": "Standard",
  "dateAjout": 1700644800000
}
```

### Autres Exemples

#### Panier 2: Souris avec emballage cadeau
```json
{
  "numPanier": "cart_abc123def457",
  "idArt": "prod_002_mouse",
  "idInt": "abc123def456ghi789",
  "quantité": 2,
  "emballage": "Gift Wrap",
  "dateAjout": 1700644900000
}
```

#### Panier 3: Clavier fragile
```json
{
  "numPanier": "cart_abc123def458",
  "idArt": "prod_003_keyboard",
  "idInt": "abc123def456ghi789",
  "quantité": 1,
  "emballage": "Fragile",
  "dateAjout": 1700645000000
}
```

---

## 📊 RÉSUMÉ DES TYPES DE DONNÉES

| Type | Description | Exemple |
|------|-------------|---------|
| **String** | Texte | `"rayen"`, `"Tunisia"`, `"Standard"` |
| **Number (Integer)** | Nombre entier | `15`, `2`, `50` |
| **Number (Double)** | Nombre décimal | `1299.99`, `99.99` |
| **Number (Long)** | Timestamp en millisecondes | `1700644800000` |

---

## 🔑 CLÉS PRIMAIRES (Identificateurs Uniques)

### Internaute
- **Clé Primaire**: `idInt` (Firebase Auth UID)
- **Unique**: `email`

### Article
- **Clé Primaire**: `idArt` (Auto-généré)

### Panier
- **Clé Primaire**: `numPanier` (Auto-généré)
- **Clés Étrangères**:
  - `idArt` → référence à `Article.idArt`
  - `idInt` → référence à `Internaute.idInt`

---

## 🔗 RELATIONS ENTRE COLLECTIONS

```
Internaute (1) ──── (N) Panier
                      ├─ idInt → Internaute.idInt
                      └─ idArt → Article.idArt

Article (1) ──── (N) Panier
                   └─ idArt → Article.idArt
```

---

## 📝 COMMENT CRÉER LES DOCUMENTS MANUELLEMENT

### 1. Créer un Utilisateur

```
Collection: Internaute
Document ID: abc123def456ghi789
Champs:
  idInt: "abc123def456ghi789"
  login: "rayen"
  email: "rayen@example.com"
  dateInscrip: 1700644800000
  pays: "Tunisia"
  role: "user"
```

### 2. Créer un Produit

```
Collection: Article
Document ID: prod_001_laptop
Champs:
  idArt: "prod_001_laptop"
  libArt: "Dell XPS 13 Laptop"
  prixArt: 1299.99
  catArt: "Laptops"
  description: "Intel i7, 16GB RAM, 512GB SSD"
  stock: 15
  imageUrl: "https://example.com/images/dell-xps.jpg"
```

### 3. Créer un Panier

```
Collection: Panier
Document ID: cart_abc123def456
Champs:
  numPanier: "cart_abc123def456"
  idArt: "prod_001_laptop"
  idInt: "abc123def456ghi789"
  quantité: 1
  emballage: "Standard"
  dateAjout: 1700644800000
```

---

## 🎯 RÈGLES DE VALIDATION

### Internaute
- ✅ `idInt`: Non vide (auto-généré par Firebase)
- ✅ `login`: 3+ caractères, non vide
- ✅ `email`: Format email valide, unique
- ✅ `dateInscrip`: Timestamp valide (>0)
- ✅ `pays`: Non vide
- ✅ `role`: "user" ou "admin"

### Article
- ✅ `idArt`: Non vide, unique
- ✅ `libArt`: Non vide, 3+ caractères
- ✅ `prixArt`: > 0
- ✅ `catArt`: Non vide
- ✅ `description`: Non vide
- ✅ `stock`: >= 0
- ✅ `imageUrl`: URL valide ou vide

### Panier
- ✅ `numPanier`: Non vide, unique
- ✅ `idArt`: Référence valide dans Article
- ✅ `idInt`: Référence valide dans Internaute
- ✅ `quantité`: > 0
- ✅ `emballage`: Une des 4 options valides
- ✅ `dateAjout`: Timestamp valide (>0)

---

## 📱 STRUCTURE DE BASE PRÊTE À L'EMPLOI

Pour tester l'application, créez:

### 1 Admin (Internaute)
```json
{
  "idInt": "admin_uid_123",
  "login": "admin",
  "email": "admin@tijara.com",
  "dateInscrip": 1700644800000,
  "pays": "Tunisia",
  "role": "admin"
}
```

### 1 Utilisateur Normal (Internaute)
```json
{
  "idInt": "user_uid_456",
  "login": "testuser",
  "email": "test@tijara.com",
  "dateInscrip": 1700644800000,
  "pays": "Tunisia",
  "role": "user"
}
```

### 3-5 Produits (Article)
```json
{
  "idArt": "laptop_001",
  "libArt": "Gaming Laptop",
  "prixArt": 1499.99,
  "catArt": "Laptops",
  "description": "High performance gaming laptop",
  "stock": 10,
  "imageUrl": "url_here"
}
```

Cela crée une base de données complète pour tester toutes les fonctionnalités! 🚀

