# TIJARA E-Commerce Application - Implementation Plan

## Project Overview
TIJARA is an Android e-commerce application for selling computer hardware. It includes user authentication, product management, shopping cart, and admin dashboard.

## Database Schema (Firebase Firestore)

### 1. Internaute (Users)
- **idInt** (Auto-generated ID)
- **login** (String)
- **password** (String - hashed by Firebase Auth)
- **dateInscrip** (Timestamp)
- **pays** (String)
- **role** (String: "user" or "admin")
- **email** (String)

### 2. Article (Products)
- **idArt** (Auto-generated ID)
- **libArt** (String - product name)
- **prixArt** (Double - price)
- **catArt** (String - category)
- **description** (String)
- **stock** (Integer)
- **imageUrl** (String)

### 3. Panier (Shopping Cart)
- **numPanier** (Auto-generated ID)
- **idArt** (Reference to Article)
- **idInt** (Reference to Internaute)
- **quantité** (Integer)
- **emballage** (String - packaging type)
- **dateAjout** (Timestamp)

## Implementation Steps

### Phase 1: Setup (Step 1-2)
- [ ] Configure Firebase project
- [ ] Create Firestore database structure

### Phase 2: Authentication (Step 3-4)
- [ ] Create Login/Register activities
- [ ] Implement Firebase Authentication
- [ ] Setup role-based access

### Phase 3: User Features (Step 5-8)
- [ ] Product list screen
- [ ] Search and filter functionality
- [ ] Shopping cart
- [ ] Product details

### Phase 4: Admin Features (Step 6-7)
- [ ] Admin dashboard
- [ ] Product CRUD operations
- [ ] User management

### Phase 5: UI/UX & Polish (Step 10-11)
- [ ] Material Design implementation
- [ ] Testing and debugging

## Key Features
1. ✅ User authentication (Login/Register)
2. ✅ Role-based access (User vs Admin)
3. ✅ Product browsing with search/filter
4. ✅ Shopping cart management
5. ✅ Admin product management
6. ✅ Admin user management
7. ✅ Modern Material Design UI

## Technologies Used
- Android Java
- Firebase Authentication
- Firebase Firestore
- Firebase Storage
- Glide (Image loading)
- Material Design 3
