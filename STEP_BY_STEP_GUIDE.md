# 🎯 TIJARA - Step-by-Step Build & Run Guide

## 📋 Before You Start
- ✅ Android Studio installed
- ✅ JDK 8 or higher
- ✅ Firebase account (free)
- ✅ Internet connection
- ✅ Project downloaded

---

## PHASE 1️⃣: Firebase Configuration (5 minutes)

### Step 1: Create Firebase Project
```
1. Go to https://console.firebase.google.com/
2. Click "Add project"
3. Project name: "TIJARA"
4. Click "Create project"
5. Wait 2-3 minutes for creation
```

### Step 2: Register Android App in Firebase
```
1. Click Android icon (or "Add app")
2. Package name: com.example.computershop
3. App nickname: TIJARA
4. SHA-1 fingerprint: (see Step 3 below)
5. Register app
6. Download google-services.json
```

### Step 3: Get SHA-1 Fingerprint
```bash
# Open PowerShell in project folder
cd c:\Users\rayen\AndroidStudioProjects\computershop
./gradlew signingReport
# Copy the SHA-1 from output
# Paste in Firebase console
```

### Step 4: Place google-services.json
```
1. Download from Firebase (Step 2)
2. Place in: app/google-services.json
3. DO NOT rename or move this file!
```

### Step 5: Enable Authentication
```
1. Firebase Console → Authentication
2. Click "Get started"
3. Select "Email/Password"
4. Toggle "Enable"
5. Click "Save"
```

### Step 6: Create Firestore Database
```
1. Firebase Console → Firestore Database
2. Click "Create database"
3. Select "Start in test mode"
4. Choose nearest region
5. Click "Enable"
6. Wait for creation (1-2 minutes)
```

### Step 7: Create Firestore Collections
```
In Firestore, create 3 empty collections:
1. Collection name: "Internaute" → Create
2. Collection name: "Article" → Create
3. Collection name: "Panier" → Create
(Don't add any documents yet)
```

---

## PHASE 2️⃣: Android Studio Setup (3 minutes)

### Step 1: Open Project in Android Studio
```
1. Launch Android Studio
2. File → Open
3. Select: c:\Users\rayen\AndroidStudioProjects\computershop
4. Click OK
5. Wait for project to load
```

### Step 2: Gradle Sync
```
1. Wait for "Gradle sync" to complete automatically
2. Or: File → Sync Now
3. Look for "Gradle build finished" at bottom
4. If errors: Clean project
   File → Invalidate Caches... → Invalidate and Restart
```

### Step 3: Verify gradle.properties
```
Open: gradle.properties
Ensure line exists: android.useAndroidX=true
```

---

## PHASE 3️⃣: Build Project (2 minutes)

### Step 1: Clean Build
```bash
./gradlew clean
./gradlew build
```

### Step 2: Wait for Build
```
Look for: "BUILD SUCCESSFUL" in console
If errors: Check Firebase setup (Phase 1)
```

---

## PHASE 4️⃣: Run on Device/Emulator (2 minutes)

### Option A: Run on Emulator
```
1. Android Studio → Device Manager (on right)
2. Click Play icon to start emulator
3. Wait for Android boot (2-3 minutes)
4. Click green Run button (or Shift+F10)
5. Select emulator and click OK
6. App launches!
```

### Option B: Run on Physical Device
```
1. Connect Android phone via USB
2. Enable Developer Mode (Settings)
3. Enable USB Debugging
4. Click green Run button in Android Studio
5. Select device
6. App launches!
```

---

## PHASE 5️⃣: First Launch & Testing (5 minutes)

### Step 1: Register Test User
```
1. App opens to Login screen
2. Click "Register" link
3. Fill in:
   - Login: testuser
   - Email: test@tijara.com
   - Password: Test@123
   - Confirm: Test@123
   - Country: Tunisia
4. Click "Register"
5. Should see success message
6. Redirects to Login
```

### Step 2: Login
```
1. Email: test@tijara.com
2. Password: Test@123
3. Click "Login"
4. Should redirect to Product List (user) or Admin (admin)
```

### Step 3: Test User Features
```
1. See product grid (currently empty - add via admin)
2. Search bar (searches when products exist)
3. Click product → product detail
4. Add to cart with quantity
5. Click "Cart" → see cart items
6. Click "Remove" → item removed
7. Click "Logout" → back to login
```

---

## PHASE 6️⃣: Add Sample Data (5 minutes)

### Step 1: Create Admin Account
```
1. Logout if logged in
2. Register new account:
   - Email: admin@tijara.com
   - Password: Admin@123
3. Verify in Firestore
```

### Step 2: Set Admin Role in Firebase
```
1. Firebase Console → Firestore
2. Collection "Internaute"
3. Find document with email: admin@tijara.com
4. Click document
5. Click "role" field → Edit → Change to "admin"
6. Save
```

### Step 3: Add Sample Products
```
1. Login as admin@tijara.com
2. Click "Manage Products"
3. Click "Add" button
4. Fill:
   - Name: Intel Core i7-13700K
   - Price: 429.99
   - Category: Processors
   - Description: 13th Gen Intel Processor
   - Stock: 15
   - Image URL: (leave blank)
5. Click "Add"
6. Repeat for 2-3 more products
```

---

## PHASE 7️⃣: Full Feature Testing (10 minutes)

### User Features Test
```
✅ Login as test@tijara.com
✅ See products list
✅ Search for product
✅ Click product → view details
✅ Add to cart with quantity
✅ View cart items
✅ Calculate total price
✅ Remove item from cart
✅ Place order
✅ Logout
```

### Admin Features Test
```
✅ Login as admin@tijara.com
✅ See Admin Dashboard
✅ Click "Manage Products"
✅ Add new product
✅ Edit existing product
✅ Delete product
✅ Click "Manage Users"
✅ Edit user role
✅ Delete user
✅ Logout
```

---

## ✅ Troubleshooting Quick Fix

### Issue: Gradle Build Fails
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Issue: App Won't Launch
```
1. Check logcat (bottom of Android Studio)
2. Look for red error messages
3. Common: Firebase not configured
   → Verify google-services.json in app/ folder
4. Common: Firestore not created
   → Create in Firebase Console
```

### Issue: Products Not Showing
```
1. Verify Firestore collections exist
2. Verify collection names: "Internaute", "Article", "Panier"
3. Check internet connection
4. Try refreshing app (close & reopen)
```

### Issue: Can't Login
```
1. Check email is registered in Firebase Auth
2. Verify password is correct
3. Check internet connection
4. Try registering new account
```

---

## 📱 App Flow Diagram

```
┌─────────────────────────────────────────────────────┐
│              TIJARA E-Commerce App                 │
└─────────────────────────────────────────────────────┘
                        ↓
            ┌──────────────────────┐
            │   Login Activity     │
            └──────────────────────┘
                  ↙            ↘
          Login         Register
            ↙              ↘
    ┌──────────────┐   ┌────────────────┐
    │ Firebase Auth│   │ Create Account │
    └──────────────┘   └────────────────┘
           ↓                    ↓
      ┌────────────────────────────────────┐
      │ Check User Role (user vs admin)   │
      └────────────────────────────────────┘
           ↙                        ↘
    ┌──────────────────────┐  ┌──────────────────────┐
    │ ProductListActivity  │  │AdminDashboardActivity│
    │ (Regular User)       │  │ (Admin User)         │
    └──────────────────────┘  └──────────────────────┘
         ↙    ↘    ↓                ↙          ↘
    Search  Cart Products        Products    Users
     ↓      ↓    ↓               Management   Mgmt
    Filter  Items Detail Add/Edit
```

---

## 📊 Database Schema Quick Reference

### Internaute (Users)
```
idInt: "auto"
login: "username"
email: "user@example.com"
dateInscrip: timestamp
pays: "country"
role: "user" or "admin"
```

### Article (Products)
```
idArt: "auto"
libArt: "Product Name"
prixArt: 99.99
catArt: "Category"
description: "Description"
stock: 50
imageUrl: "https://..."
```

### Panier (Cart)
```
numPanier: "auto"
idArt: "product-id"
idInt: "user-id"
quantité: 2
emballage: "Standard"
dateAjout: timestamp
```

---

## 🎯 Success Indicators

✅ **App Launches** - Sees login screen  
✅ **Registration Works** - Creates user  
✅ **Login Works** - Authenticates user  
✅ **Products Show** - Lists items  
✅ **Cart Works** - Adds/removes items  
✅ **Admin Access** - Admin dashboard visible  
✅ **CRUD Works** - Can add/edit/delete products  

---

## 📚 Documentation Files to Read

```
1. README.md                    → Complete overview
2. COMPLETE_SETUP_GUIDE.md      → Detailed setup
3. QUICK_START.md               → Commands reference
4. BUILD_SUMMARY.md             → What was built
5. IMPLEMENTATION_PLAN.md       → Architecture
```

---

## 🚀 You're Ready!

All code is written and ready to run. Just follow these phases:

1. ✅ Firebase setup (5 min)
2. ✅ Android Studio setup (3 min)
3. ✅ Build project (2 min)
4. ✅ Run on device (2 min)
5. ✅ Test features (5 min)
6. ✅ Add sample data (5 min)
7. ✅ Full testing (10 min)

**Total: ~30 minutes from start to fully working app!**

---

## 💡 Pro Tips

- Start with small features and test incrementally
- Use Firebase Console to verify data is being saved
- Check logcat for detailed error messages
- Create test accounts with simple passwords for testing
- Add sample products through admin interface
- Test with multiple user accounts
- Verify Firestore rules allow test mode access

---

**Now Go Build Your App! 🎉**

Questions? Check the troubleshooting sections in the documentation files!
