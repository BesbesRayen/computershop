# TIJARA E-Commerce App - Complete Setup & Build Guide

## 📋 Project Overview
TIJARA is a complete Android e-commerce application for buying/selling computer hardware with:
- User authentication (Login/Register)
- Role-based access control (User/Admin)
- Product management with search/filter
- Shopping cart functionality
- Admin dashboard for CRUD operations
- Material Design UI

---

## 🔧 STEP 1: Firebase Setup (CRITICAL)

### 1.1 Create Firebase Project
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project"
3. Name it: "TIJARA" (or your preferred name)
4. Enable Google Analytics (optional)
5. Create the project

### 1.2 Register Android App
1. In Firebase Console, click the Android icon
2. Package name: `com.example.computershop`
3. App nickname: `TIJARA`
4. SHA-1 fingerprint: Run this command in terminal:
   ```bash
   ./gradlew signingReport
   ```
   Copy the SHA-1 from the output

5. Download `google-services.json`
6. Place it in: `app/google-services.json`

### 1.3 Enable Firebase Services
In Firebase Console → Project Settings:

1. **Authentication**
   - Enable "Email/Password"
   - Go to Authentication → Sign-in method
   - Enable Email/Password provider

2. **Firestore Database**
   - Create Database
   - Start in "Test mode"
   - Choose region closest to you
   - Create

3. **Firestore Security Rules** (for testing - make stricter for production):
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

---

## 📱 STEP 2: Android Studio Setup

### 2.1 Open Project
1. Open Android Studio
2. Open the project: `c:\Users\rayen\AndroidStudioProjects\computershop`

### 2.2 Verify gradle.properties
Ensure `android.useAndroidX=true` is set:
```bash
Location: gradle.properties
```

### 2.3 Sync Gradle
1. File → Sync Now
2. Wait for gradle sync to complete
3. Resolve any dependency issues

---

## 🏗️ STEP 3: Build & Run

### 3.1 Build the Project
```bash
# In terminal/PowerShell at project root
./gradlew clean
./gradlew build
```

### 3.2 Run on Emulator/Device
1. Create/open Android Virtual Device (AVD)
2. Click Run button or:
   ```bash
   ./gradlew installDebug
   ```

---

## 📊 STEP 4: Initialize Firebase Data

After first app launch, create sample data:

### 4.1 Manual Firestore Setup (via Console)
Go to Firebase Console → Firestore Database:

**Create Collection: "Article"** (Products)
Add documents with these fields:
```json
{
  "catArt": "Processors",
  "description": "Intel i7 13th Gen",
  "idArt": "AUTO_ID",
  "libArt": "Intel Core i7-13700K",
  "prixArt": 429.99,
  "stock": 15,
  "imageUrl": "https://example.com/image.jpg"
}
```

**Create Collection: "Internaute"** (Users)
Auto-generated when users register

**Create Collection: "Panier"** (Cart)
Auto-generated when items added to cart

---

## 🎯 STEP 5: Testing the Application

### 5.1 Test Account Credentials
After registering, use these test accounts:

**Admin Account:**
- Email: admin@tijara.com
- Password: Admin123!
- (Set role to "admin" in Firebase Console)

**Regular User:**
- Email: user@tijara.com
- Password: User123!

### 5.2 Feature Testing

1. **Registration**
   - Click "Register" → Fill form → Register
   - Verify user appears in Firestore

2. **Login**
   - Login with credentials
   - Redirects to admin dashboard (if admin) or product list (if user)

3. **Product List (User)**
   - Browse products
   - Search by name/category/price
   - Click product for details
   - Add to cart with quantity & packaging

4. **Cart**
   - View all items
   - See total price
   - Remove items
   - "Place Order" (simulates purchase)

5. **Admin Dashboard**
   - Click "Manage Products"
   - Add new product
   - Edit existing product
   - Delete product
   - Click "Manage Users"
   - Edit user role
   - Delete user

---

## 🔐 STEP 6: Firebase Security (For Production)

Replace test mode rules with:
```
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /Internaute/{userId} {
      allow read, write: if request.auth.uid == userId;
      allow read: if request.auth.uid != null && 
                     get(/databases/$(database)/documents/Internaute/$(request.auth.uid)).data.role == 'admin';
    }
    match /Article/{document=**} {
      allow read: if request.auth != null;
      allow write: if request.auth != null && 
                      get(/databases/$(database)/documents/Internaute/$(request.auth.uid)).data.role == 'admin';
    }
    match /Panier/{document=**} {
      allow read, write: if request.auth.uid != null;
    }
  }
}
```

---

## 📁 Project Structure

```
computershop/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/computershop/
│   │   │   │   ├── activities/
│   │   │   │   │   ├── LoginActivity.java
│   │   │   │   │   ├── RegisterActivity.java
│   │   │   │   │   ├── ProductListActivity.java
│   │   │   │   │   ├── ProductDetailActivity.java
│   │   │   │   │   ├── CartActivity.java
│   │   │   │   │   ├── AdminDashboardActivity.java
│   │   │   │   │   ├── AdminProductsActivity.java
│   │   │   │   │   └── AdminUsersActivity.java
│   │   │   │   ├── models/
│   │   │   │   │   ├── User.java
│   │   │   │   │   ├── Product.java
│   │   │   │   │   └── CartItem.java
│   │   │   │   ├── adapters/
│   │   │   │   │   ├── ProductAdapter.java
│   │   │   │   │   ├── CartAdapter.java
│   │   │   │   │   ├── AdminProductAdapter.java
│   │   │   │   │   └── UserAdapter.java
│   │   │   │   └── utils/
│   │   │   │       └── FirebaseManager.java
│   │   │   ├── res/
│   │   │   │   ├── layout/
│   │   │   │   │   ├── activity_login.xml
│   │   │   │   │   ├── activity_register.xml
│   │   │   │   │   ├── activity_product_list.xml
│   │   │   │   │   ├── activity_product_detail.xml
│   │   │   │   │   ├── activity_cart.xml
│   │   │   │   │   ├── activity_admin_dashboard.xml
│   │   │   │   │   ├── activity_admin_products.xml
│   │   │   │   │   ├── activity_admin_users.xml
│   │   │   │   │   ├── item_product.xml
│   │   │   │   │   ├── item_cart.xml
│   │   │   │   │   ├── item_admin_product.xml
│   │   │   │   │   └── item_user.xml
│   │   │   │   ├── drawable/
│   │   │   │   │   ├── button_background.xml
│   │   │   │   │   ├── edit_text_background.xml
│   │   │   │   │   └── gradient_background.xml
│   │   │   │   └── values/
│   │   │   │       ├── strings.xml
│   │   │   │       └── colors.xml
│   │   │   └── AndroidManifest.xml
│   └── google-services.json (IMPORTANT!)
└── build.gradle.kts
```

---

## ✅ Verification Checklist

- [ ] Firebase Project Created
- [ ] google-services.json Added
- [ ] Firebase Auth Enabled
- [ ] Firestore Database Created
- [ ] Gradle Sync Completed
- [ ] Project Builds Successfully
- [ ] App Runs on Emulator/Device
- [ ] Can Register New User
- [ ] Can Login with Credentials
- [ ] User Data Appears in Firestore
- [ ] Product List Shows
- [ ] Can Add Product to Cart (Admin)
- [ ] Can View Cart Items
- [ ] Admin Dashboard Works

---

## 🐛 Troubleshooting

### Issue: "google-services.json not found"
- Place `google-services.json` in `app/` folder

### Issue: Firebase Authentication Errors
- Check Firebase console → Authentication → Email/Password enabled

### Issue: Firestore Connection Failed
- Check Firestore is created
- Verify security rules allow read/write in test mode

### Issue: App Crashes on Login
- Check Firebase has internet permission in AndroidManifest.xml
- Verify Firebase initialization in code

### Issue: RecyclerView Not Showing
- Verify adapter is attached
- Check Firestore data format matches model

---

## 📚 Key Features Implemented

✅ **Authentication**
- User registration with validation
- Email/password login
- Password confirmation
- Role-based login routing

✅ **User Features**
- View all products
- Search products (name, category, price)
- View product details
- Add to cart with quantity & packaging
- View shopping cart
- Remove cart items
- Calculate total price

✅ **Admin Features**
- Add new products
- Edit existing products
- Delete products
- View all users
- Edit user roles
- Delete users
- Logout

✅ **UI/UX**
- Material Design 3
- Gradient backgrounds
- Card views for products
- Responsive layouts
- Color-coded buttons
- Toast notifications

---

## 🚀 Future Enhancements

- [ ] Payment integration (Stripe, PayPal)
- [ ] Order history tracking
- [ ] Product reviews and ratings
- [ ] Wishlist feature
- [ ] Push notifications
- [ ] Real-time inventory updates
- [ ] User profile management
- [ ] Order status tracking
- [ ] PDF invoice generation
- [ ] Multi-language support

---

## 📞 Support

For issues or questions:
1. Check Firestore data structure
2. Verify Firebase configuration
3. Check logcat for error messages
4. Ensure internet connection

---

**Happy Coding! 🎉**
