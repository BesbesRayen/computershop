# TIJARA - Quick Start Commands

## For Windows PowerShell

### Clean Build
```powershell
cd c:\Users\rayen\AndroidStudioProjects\computershop
./gradlew clean build
```

### Run on Emulator/Device
```powershell
./gradlew installDebug
# Or through Android Studio
```

### Build APK (Release)
```powershell
./gradlew assembleRelease
```

### Run Tests
```powershell
./gradlew test
```

### Check Dependencies
```powershell
./gradlew dependencies
```

### Get SHA-1 for Firebase
```powershell
./gradlew signingReport
```

---

## Firebase Console Checklist

Before running the app, complete:

1. **Create Firebase Project**
   - Go to https://console.firebase.google.com/
   - Create new project "TIJARA"

2. **Register Android App**
   - Package: `com.example.computershop`
   - Download `google-services.json`
   - Save to: `app/google-services.json`

3. **Enable Authentication**
   - Firebase Console → Authentication
   - Enable "Email/Password" provider

4. **Create Firestore Database**
   - Firebase Console → Firestore Database
   - Start in "Test mode"
   - Create collections: "Internaute", "Article", "Panier"

5. **Update Security Rules**
   - Copy rules from COMPLETE_SETUP_GUIDE.md

---

## Gradle Sync Issues?

If you see errors:
```powershell
# Clear gradle cache
./gradlew clean

# Rebuild
./gradlew build

# Sync IDE
# In Android Studio: File → Sync Now
```

---

## Database Schema

### Users Collection (Internaute)
```json
{
  "idInt": "AUTO",
  "login": "string",
  "email": "string",
  "dateInscrip": "timestamp",
  "pays": "string",
  "role": "user|admin"
}
```

### Products Collection (Article)
```json
{
  "idArt": "AUTO",
  "libArt": "string",
  "prixArt": "number",
  "catArt": "string",
  "description": "string",
  "stock": "number",
  "imageUrl": "string"
}
```

### Cart Collection (Panier)
```json
{
  "numPanier": "AUTO",
  "idArt": "reference",
  "idInt": "reference",
  "quantité": "number",
  "emballage": "string",
  "dateAjout": "timestamp"
}
```

---

## Test Credentials

Create these in Firebase Console after first run:

**Admin User:**
- Email: admin@tijara.com
- Password: Admin@123
- Role: admin

**Regular User:**
- Email: user@tijara.com
- Password: User@123
- Role: user

---

## Sample Product Data

Insert into Firestore "Article" collection:

```json
[
  {
    "libArt": "Intel Core i7-13700K",
    "prixArt": 429.99,
    "catArt": "Processors",
    "description": "13th Generation Intel Processor",
    "stock": 15,
    "imageUrl": ""
  },
  {
    "libArt": "RTX 4080 Graphics Card",
    "prixArt": 1199.99,
    "catArt": "Graphics Cards",
    "description": "NVIDIA RTX 4080 Super",
    "stock": 8,
    "imageUrl": ""
  },
  {
    "libArt": "16GB DDR5 RAM",
    "prixArt": 79.99,
    "catArt": "Memory",
    "description": "High-speed DDR5 Memory",
    "stock": 50,
    "imageUrl": ""
  }
]
```

---

## App Flow

1. **Splash Screen** (LoginActivity)
   - Check if user logged in
   - Redirect to login/register

2. **Login/Register**
   - Create account OR Login
   - Redirect based on role

3. **User Dashboard** (ProductListActivity)
   - View products
   - Search/Filter
   - Add to cart
   - View cart

4. **Admin Dashboard** (AdminDashboardActivity)
   - Manage products (CRUD)
   - Manage users (Edit role, Delete)
   - Logout

---

## Key Classes

- **FirebaseManager**: All Firebase operations
- **LoginActivity**: Authentication
- **RegisterActivity**: User signup
- **ProductListActivity**: Browse products
- **ProductDetailActivity**: Product info & add to cart
- **CartActivity**: Shopping cart management
- **AdminDashboardActivity**: Admin home
- **AdminProductsActivity**: Product management
- **AdminUsersActivity**: User management

---

**See COMPLETE_SETUP_GUIDE.md for detailed instructions!**
