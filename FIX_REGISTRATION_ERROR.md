# 🐛 FIX REGISTRATION ERROR

## The Problem
Registration is failing because **Firestore collections don't exist** in your Firebase project.

---

## ✅ SOLUTION: Create Collections in Firebase

### Step 1: Go to Firebase Console
```
1. Open: https://console.firebase.google.com/
2. Select your project: "TIJARA" (or your project name)
3. Left menu: Click "Firestore Database"
4. You should see your database created
```

### Step 2: Create Collection "Internaute" (Users)

```
1. In Firestore, click "Create collection"
2. Collection ID: Internaute
3. Click "Next"
4. You'll see "Add a document" - SKIP THIS (click "Create" without adding)
5. Collection "Internaute" is now created
```

### Step 3: Create Collection "Article" (Products)

```
1. Click "+ Create collection" again
2. Collection ID: Article
3. Click "Next"
4. SKIP adding document (click "Create")
5. Collection "Article" is now created
```

### Step 4: Create Collection "Panier" (Shopping Cart)

```
1. Click "+ Create collection" again
2. Collection ID: Panier
3. Click "Next"
4. SKIP adding document (click "Create")
5. Collection "Panier" is now created
```

### Step 5: Verify Collections

You should now see in Firestore:
```
✓ Internaute  (for users)
✓ Article     (for products)
✓ Panier      (for shopping cart)
```

---

## ✅ SOLUTION 2: Enable Firebase Services

### Make sure Email/Password is Enabled

```
1. Left menu: Authentication
2. Click "Get started"
3. Find "Email/Password"
4. Click it
5. Toggle "Enable" (should be blue)
6. Click "Save"
```

---

## ✅ NOW TRY AGAIN

1. Close the app (back button or close emulator)
2. In Android Studio, click Run button (▶)
3. Try to Register again with:
   - Login: testuser
   - Email: test@example.com
   - Password: Test@123
   - Confirm: Test@123
   - Country: Tunisia
4. Click Register

**You should see: "Registration successful!"**

---

## 🐛 Still Failing?

### Check These:

1. **Internet Connection**
   - Emulator has internet access
   - WiFi/mobile data is on

2. **Firebase Config**
   - `google-services.json` is in `app/` folder
   - Not corrupted or incomplete

3. **Collections Exist**
   - Go to Firestore
   - See "Internaute", "Article", "Panier" in collections

4. **Email/Password Auth Enabled**
   - Authentication → Email/Password → Enabled (blue toggle)

---

## 📱 Test Flow

```
1. App launches → Login screen
2. Click "Register"
3. Fill form (example above)
4. Click "Register"
5. See "Registration successful!"
6. Redirected to Login screen
7. Login with test@example.com / Test@123
8. See Product List screen

✅ If you see Product List = SUCCESS!
```

---

## 💡 Common Errors & Fixes

### Error: "Registration failed: The email address is already in use"
- Try different email (add random number at end)
- Example: test123@example.com, test456@example.com

### Error: "Registration failed: Quotas exceeded"
- Collections exist but have limits
- Probably just try again in a few seconds

### Error: "No network connection"
- Check emulator has internet
- In emulator settings, ensure WiFi is simulated

### Error: "Invalid JSON in google-services.json"
- Re-download from Firebase console
- Place in app/ folder
- Sync Gradle

---

## ✅ Quick Checklist

- [ ] Created Firestore database
- [ ] Created "Internaute" collection
- [ ] Created "Article" collection
- [ ] Created "Panier" collection
- [ ] Email/Password auth enabled in Firebase
- [ ] google-services.json in app/ folder
- [ ] App rebuilds successfully
- [ ] App launches without crash
- [ ] Registration form appears
- [ ] Can enter test data
- [ ] See success message after clicking Register

**Once all checked: Try registering again! 🚀**

