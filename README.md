# 🛍️ TIJARA - Computer Hardware E-Commerce Application

A complete Android e-commerce application for buying and selling computer hardware with Firebase backend, user authentication, and role-based access control.

![Version](https://img.shields.io/badge/version-1.0-blue)
![Android](https://img.shields.io/badge/Android-API%2024+-brightgreen)
![Firebase](https://img.shields.io/badge/Firebase-Firestore-orange)

---

## 📋 Table of Contents
1. [Overview](#overview)
2. [Features](#features)
3. [Requirements](#requirements)
4. [Installation](#installation)
5. [Firebase Setup](#firebase-setup)
6. [Building & Running](#building--running)
7. [Project Structure](#project-structure)
8. [Database Schema](#database-schema)
9. [Testing](#testing)
10. [Troubleshooting](#troubleshooting)

---

## 🎯 Overview

**TIJARA** is a full-featured e-commerce mobile application built with:
- **Frontend**: Android Java (Material Design 3)
- **Backend**: Firebase (Authentication + Firestore)
- **Architecture**: MVVM pattern with separation of concerns
- **Features**: Product management, shopping cart, user authentication, admin dashboard

### Technology Stack
- **Language**: Java 8
- **Target SDK**: Android 14 (API 34)
- **Min SDK**: Android 7 (API 24)
- **Build System**: Gradle
- **Firebase**: Auth + Firestore + Storage
- **UI**: Material Design 3, CardView, RecyclerView

---

## ✨ Features

### 👤 User Features
- ✅ User Registration with validation
- ✅ Secure Email/Password Authentication
- ✅ View Product Catalog
- ✅ Search Products (by name, category, price)
- ✅ View Product Details
- ✅ Add Products to Shopping Cart
- ✅ Manage Shopping Cart (view, edit quantity, remove items)
- ✅ Calculate Total Price
- ✅ Place Orders
- ✅ Logout

### 👨‍💼 Admin Features
- ✅ Complete Product Management (Create, Read, Update, Delete)
- ✅ User Management Dashboard
- ✅ Edit User Roles
- ✅ Delete Users
- ✅ Inventory Management
- ✅ Admin Statistics
- ✅ Secure Logout

### 🎨 UI/UX Features
- ✅ Material Design 3 Interface
- ✅ Gradient Backgrounds
- ✅ Responsive Layouts
- ✅ Card-based Product Display
- ✅ Real-time Search
- ✅ Toast Notifications
- ✅ Dialog Boxes for Actions
- ✅ Grid and List Views

---

## 📦 Requirements

### Software Requirements
- **Android Studio**: 4.2 or higher
- **JDK**: Java 8 or higher
- **Gradle**: 8.2.0 or higher
- **Firebase Account**: Free tier available

### Hardware Requirements
- **RAM**: 4GB minimum
- **Disk Space**: 2GB for Android Studio + 500MB for project

---

## 🔧 Installation

### Step 1: Clone/Extract Project
```bash
cd c:\Users\rayen\AndroidStudioProjects\computershop
```

### Step 2: Open in Android Studio
1. Launch Android Studio
2. File → Open
3. Navigate to the project folder
4. Click OK

### Step 3: Gradle Sync
1. Wait for Gradle sync to complete
2. If there are issues: File → Invalidate Caches and Restart

---

## 🔐 Firebase Setup (IMPORTANT!)

### Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project"
3. Project name: "TIJARA"
4. Enable Google Analytics (optional)
5. Create Project

### Register Android App
1. In Firebase Console, click the Android icon
2. **Package name**: `com.example.computershop`
3. **App nickname**: TIJARA
4. **Get SHA-1**: Run in terminal:
   ```bash
   ./gradlew signingReport
   ```
5. Copy SHA-1 fingerprint
6. Continue registration
7. Download `google-services.json`
8. **Place in**: `app/google-services.json` (CRITICAL!)

### Enable Firebase Services

#### Authentication
1. Firebase Console → Authentication
2. Click "Get started"
3. Select "Email/Password"
4. Enable and Save

#### Firestore Database
1. Firebase Console → Firestore Database
2. Click "Create database"
3. Select "Start in test mode"
4. Choose region (nearest to you)
5. Click "Enable"

#### Security Rules (Test Mode - Change for Production!)
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

### Create Collections
In Firestore, create three collections:
1. **Internaute** (Users collection)
2. **Article** (Products collection)
3. **Panier** (Shopping cart collection)

---

## 🚀 Building & Running

### Build the Project
```bash
# Clean build
./gradlew clean

# Build
./gradlew build

# Build and install to device/emulator
./gradlew installDebug
```

### Run on Emulator/Device
1. **Create AVD** (Android Virtual Device):
   - Android Studio → Device Manager
   - Create new device
   - Choose API level 30 or higher
   - Start emulator

2. **Run App**:
   - Click green "Run" button in Android Studio
   - Or use: `./gradlew installDebug`

### Build Release APK
```bash
./gradlew assembleRelease
# APK location: app/build/outputs/apk/release/
```

---

## 📁 Project Structure

```
computershop/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/computershop/
│   │   │   │   ├── activities/          # Activity classes
│   │   │   │   │   ├── LoginActivity
│   │   │   │   │   ├── RegisterActivity
│   │   │   │   │   ├── ProductListActivity
│   │   │   │   │   ├── ProductDetailActivity
│   │   │   │   │   ├── CartActivity
│   │   │   │   │   ├── AdminDashboardActivity
│   │   │   │   │   ├── AdminProductsActivity
│   │   │   │   │   └── AdminUsersActivity
│   │   │   │   ├── models/              # Data models
│   │   │   │   │   ├── User
│   │   │   │   │   ├── Product
│   │   │   │   │   └── CartItem
│   │   │   │   ├── adapters/            # RecyclerView adapters
│   │   │   │   │   ├── ProductAdapter
│   │   │   │   │   ├── CartAdapter
│   │   │   │   │   ├── AdminProductAdapter
│   │   │   │   │   └── UserAdapter
│   │   │   │   └── utils/               # Utility classes
│   │   │   │       └── FirebaseManager
│   │   │   ├── res/
│   │   │   │   ├── layout/              # XML layout files
│   │   │   │   ├── drawable/            # Drawable resources
│   │   │   │   ├── values/              # String resources
│   │   │   │   └── mipmap/              # App icons
│   │   │   └── AndroidManifest.xml
│   │   ├── test/                        # Unit tests
│   │   └── androidTest/                 # Instrumented tests
│   ├── build.gradle.kts                 # App-level gradle config
│   └── google-services.json             # Firebase config (add this!)
├── gradle/                              # Gradle wrapper
├── build.gradle.kts                     # Project-level gradle config
├── settings.gradle.kts
└── gradlew / gradlew.bat

```

---

## 🗄️ Database Schema

### Users Collection (Internaute)
```json
{
  "idInt": "auto-generated",
  "login": "username",
  "email": "user@example.com",
  "dateInscrip": 1234567890000,
  "pays": "Tunisia",
  "role": "user"  // "user" or "admin"
}
```

### Products Collection (Article)
```json
{
  "idArt": "auto-generated",
  "libArt": "Product Name",
  "prixArt": 99.99,
  "catArt": "Category",
  "description": "Product Description",
  "stock": 50,
  "imageUrl": "https://example.com/image.jpg"
}
```

### Shopping Cart Collection (Panier)
```json
{
  "numPanier": "auto-generated",
  "idArt": "product-id",
  "idInt": "user-id",
  "quantité": 2,
  "emballage": "Standard",
  "dateAjout": 1234567890000
}
```

---

## 🧪 Testing

### Test User Creation
Create test accounts in Firebase Console:

#### Admin Test Account
```
Email: admin@tijara.com
Password: Admin@123
(Set role to "admin" in Firestore)
```

#### Regular User Account
```
Email: user@tijara.com
Password: User@123
(Default role is "user")
```

### Feature Testing Checklist
- [ ] Register new user
- [ ] Login with credentials
- [ ] Search for products
- [ ] Add product to cart
- [ ] View and modify cart
- [ ] Place order
- [ ] Admin: Add new product
- [ ] Admin: Edit product
- [ ] Admin: Delete product
- [ ] Admin: Manage users

### Sample Product Data
```json
[
  {
    "libArt": "Intel Core i7-13700K",
    "prixArt": 429.99,
    "catArt": "Processors",
    "description": "13th Gen Intel",
    "stock": 15,
    "imageUrl": ""
  },
  {
    "libArt": "RTX 4080 Graphics Card",
    "prixArt": 1199.99,
    "catArt": "Graphics Cards",
    "description": "NVIDIA RTX 4080",
    "stock": 8,
    "imageUrl": ""
  },
  {
    "libArt": "16GB DDR5 RAM",
    "prixArt": 79.99,
    "catArt": "Memory",
    "description": "High-speed DDR5",
    "stock": 50,
    "imageUrl": ""
  }
]
```

---

## 🐛 Troubleshooting

### Issue: "google-services.json not found"
**Solution**: 
- Download from Firebase Console
- Place in `app/google-services.json`
- Sync Gradle

### Issue: "Failed to resolve dependency"
**Solution**:
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Issue: Firebase Authentication Not Working
**Solution**:
- Check Email/Password provider enabled in Firebase
- Verify internet permission in AndroidManifest.xml
- Check user exists in Firebase Auth console

### Issue: Firestore Data Not Showing
**Solution**:
- Verify Firestore database created
- Check security rules (test mode allows all)
- Verify collection names: "Internaute", "Article", "Panier"
- Check data format matches model classes

### Issue: App Crashes on Startup
**Solution**:
- Check logcat for error messages
- Run: `./gradlew clean build`
- Verify Firebase initialization
- Check all permissions in AndroidManifest.xml

### Issue: RecyclerView Shows No Items
**Solution**:
- Verify data in Firestore
- Check adapter is attached
- Debug logcat for query errors
- Verify model fields match Firestore document fields

### Issue: Image Won't Load
**Solution**:
- Check imageUrl is valid HTTPS
- Verify internet permission
- Ensure Glide dependency is included
- Use placeholder if URL is empty

---

## 📱 App Navigation Flow

```
START
  ↓
LoginActivity (Check if logged in)
  ├─→ Already logged in? → Redirect based on role
  │
  ├─→ ProductListActivity (User)
  │   ├─→ ProductDetailActivity (Add to cart)
  │   └─→ CartActivity
  │
  └─→ AdminDashboardActivity (Admin)
      ├─→ AdminProductsActivity (CRUD)
      └─→ AdminUsersActivity (Manage)
```

---

## 🔒 Security Best Practices

### Current (Test Mode)
- Firestore allows all read/write in test mode
- ⚠️ NOT suitable for production

### Production Security
Update Firestore rules:
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /Internaute/{userId} {
      allow read, write: if request.auth.uid == userId;
    }
    match /Article/{doc=**} {
      allow read: if request.auth != null;
      allow write: if isAdmin();
    }
    match /Panier/{doc=**} {
      allow read, write: if request.auth != null;
    }
  }
}
```

---

## 📚 Key Classes

| Class | Purpose |
|-------|---------|
| `FirebaseManager` | Central Firebase operations hub |
| `LoginActivity` | User authentication |
| `RegisterActivity` | User registration |
| `ProductListActivity` | Browse/search products |
| `ProductDetailActivity` | Product info & add to cart |
| `CartActivity` | Shopping cart management |
| `AdminDashboardActivity` | Admin home |
| `AdminProductsActivity` | Product CRUD |
| `AdminUsersActivity` | User management |
| `ProductAdapter` | RecyclerView for products |

---

## 🚀 Future Enhancements

- [ ] Payment gateway integration
- [ ] Order history tracking
- [ ] Product reviews and ratings
- [ ] Wishlist feature
- [ ] Push notifications
- [ ] Real-time inventory sync
- [ ] User profile customization
- [ ] Order tracking
- [ ] PDF invoice generation
- [ ] Multi-language support
- [ ] Dark mode
- [ ] Advanced analytics

---

## 📖 Documentation Files

- **COMPLETE_SETUP_GUIDE.md** - Detailed setup instructions
- **QUICK_START.md** - Quick reference commands
- **IMPLEMENTATION_PLAN.md** - Architecture overview

---

## 📄 License

This project is provided as-is for educational purposes.

---

## 👨‍💻 Support

For issues or questions:
1. Check logcat in Android Studio
2. Review Firebase Console
3. Verify Firestore collection structure
4. Check internet connectivity
5. Refer to troubleshooting section

---

## ✅ Verification Checklist

Before considering the app complete:

- [ ] Firebase project created
- [ ] google-services.json added
- [ ] Authentication configured
- [ ] Firestore database created
- [ ] Gradle builds successfully
- [ ] App runs on emulator/device
- [ ] Can register new user
- [ ] Can login successfully
- [ ] User data in Firestore
- [ ] Products display
- [ ] Cart functionality works
- [ ] Admin features work
- [ ] No crashes in logcat

---

**Happy Coding! 🎉**

For detailed setup steps, see **COMPLETE_SETUP_GUIDE.md**
