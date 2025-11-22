# 🚀 BUILD & RUN - FINAL INSTRUCTIONS

## ⚠️ BEFORE YOU START - CRITICAL!

You MUST complete Firebase setup first!

**See: STEP_BY_STEP_GUIDE.md → PHASE 1** (takes 5 minutes)

---

## ✅ Checklist Before Building

- [ ] Firebase project created at console.firebase.google.com
- [ ] Android app registered in Firebase (package: com.example.computershop)
- [ ] google-services.json downloaded from Firebase
- [ ] google-services.json placed in `app/` folder
- [ ] Firebase Authentication enabled (Email/Password)
- [ ] Firestore Database created
- [ ] Three collections created: Internaute, Article, Panier
- [ ] Android Studio installed and updated
- [ ] Project opened in Android Studio
- [ ] Gradle sync completed

---

## 🔨 BUILD STEPS

### Step 1: Verify google-services.json
```
File location: app/google-services.json
If missing:
  1. Download from Firebase Console
  2. Place in app/ folder (NOT in app/src!)
  3. File → Sync Now in Android Studio
```

### Step 2: Clean Project
```bash
cd c:\Users\rayen\AndroidStudioProjects\computershop
./gradlew clean
```

### Step 3: Build Project
```bash
./gradlew build
```

**Expected Output:**
```
...
BUILD SUCCESSFUL in XXs
```

**If BUILD FAILED:**
```
Check errors in console
Common issue: google-services.json missing
Solution: 
  1. Verify file in app/google-services.json
  2. Run: File → Sync Now
  3. Try building again
```

---

## 📱 RUN ON EMULATOR

### Option 1: Android Studio UI (Easiest)
```
1. Click Device Manager (right panel)
2. Create new device if needed (API 30+)
3. Click play icon to start emulator
4. Wait 2-3 minutes for boot
5. Click green Run button (or Shift+F10)
6. Select emulator from dialog
7. Click OK
8. App launches!
```

### Option 2: Command Line
```bash
./gradlew installDebug
# Automatically installs to running emulator/device
```

### Option 3: Build APK and Install
```bash
./gradlew assembleDebug
# APK created at: app/build/outputs/apk/debug/app-debug.apk

# Then install with:
adb install -r app/build/outputs/apk/debug/app-debug.apk
```

---

## 📱 RUN ON PHYSICAL DEVICE

### Step 1: Connect Device
```
1. Connect Android phone via USB cable
2. Allow USB debugging on phone
3. Device should appear in Device Manager
```

### Step 2: Enable Developer Mode
On Android phone:
```
1. Settings → About Phone
2. Tap "Build Number" 7 times
3. Back to Settings → Developer Options
4. Enable "USB Debugging"
```

### Step 3: Run App
```
1. Click green Run button in Android Studio
2. Select your device
3. Click OK
4. App launches on device!
```

---

## 🎮 FIRST TIME RUNNING

### What You'll See
1. **Login Screen**
   - Email/Password fields
   - "Register" link
   - Login button

### First Action: Register
```
1. Click "Register" link
2. Fill in form:
   - Login: testuser
   - Email: test@example.com
   - Password: Test@123
   - Confirm: Test@123
   - Country: Tunisia
3. Click "Register"
4. Message: "Registration successful!"
5. Redirects to login
```

### Second Action: Login
```
1. Email: test@example.com
2. Password: Test@123
3. Click "Login"
4. Redirects to Product List (user role)
5. See empty list (no products yet)
```

### Next: Add Sample Products (as Admin)
```
1. Register new account:
   - Email: admin@example.com
   - Make sure to note this!
2. Go to Firebase Console
3. Firestore → Collection "Internaute"
4. Find document with admin email
5. Edit "role" field → change to "admin"
6. Save
7. Logout and login as admin
8. Now see Admin Dashboard
9. Click "Manage Products"
10. Click "Add" button
11. Fill product form and save
12. See product appear!
```

---

## ✅ VERIFICATION - ALL WORKING?

### Test Checklist
- [ ] App launches without crashing
- [ ] Can register new user
- [ ] Can login with email/password
- [ ] Redirects based on role
- [ ] Can add product to cart (if user)
- [ ] Can view cart
- [ ] Can remove from cart
- [ ] Admin can add products
- [ ] Admin can edit products
- [ ] Admin can delete products
- [ ] Admin can manage users
- [ ] Can logout

### Check Firestore Data
```
1. Firebase Console → Firestore Database
2. Check collections populated:
   - Internaute: Should see user documents
   - Article: Should see product documents
   - Panier: Should see cart documents
3. If empty:
   - Check logcat for errors
   - Verify firestore rules allow write
   - Restart app
```

---

## 🐛 COMMON BUILD ERRORS

### Error: "google-services.json not found"
```
Solution:
1. Download google-services.json from Firebase
2. Place in app/ folder
3. NOT in app/src/main/!
4. File → Sync Now
5. Rebuild
```

### Error: "Failed to resolve dependency"
```bash
./gradlew build --refresh-dependencies
```

### Error: "Unable to resolve activity"
```
Check AndroidManifest.xml:
- All 8 activities are registered
- LoginActivity is launcher activity
- File syntax is correct
```

### Error: "Firebase configuration error"
```
1. Verify google-services.json in app/
2. Check package name matches: com.example.computershop
3. Try invalidating cache: File → Invalidate Caches...
```

### App Crashes on Login
```
Check logcat for:
1. Firebase Auth errors: Check authentication enabled
2. Firestore errors: Check database created
3. Permission errors: Check internet permission in manifest
```

---

## 📊 BUILD ARTIFACTS

After successful build, find:

**Debug APK**
```
app/build/outputs/apk/debug/app-debug.apk
```

**Release APK**
```bash
./gradlew assembleRelease
# Creates: app/build/outputs/apk/release/app-release.apk
```

**Build Report**
```
app/build/reports/
```

---

## ⏱️ TIMING

Expected times:
```
First gradle sync:        2-3 minutes
Clean build:              1-2 minutes
Build project:            1-2 minutes
Emulator startup:         2-3 minutes
App launch on device:     1-2 minutes
First run after changes:  1-2 minutes
─────────────────────────
Total first time:         ~10 minutes
Subsequent builds:        ~3-5 minutes
```

---

## 🎯 NEXT STEPS AFTER BUILDING

1. **Test Features**
   - Register user
   - Login/logout
   - Add products (admin)
   - Add to cart
   - View cart
   - Manage users (admin)

2. **Add Sample Data**
   - Login as admin
   - Add 5-10 products
   - Try various categories
   - Add prices and descriptions

3. **Test Thoroughly**
   - Test with multiple accounts
   - Test admin functions
   - Test cart functionality
   - Check Firebase Console for data

4. **Customize (Optional)**
   - Change app colors
   - Add company logo
   - Modify product categories
   - Adjust text/strings

---

## 📚 DOCUMENTATION REFERENCE

- **README.md** - Complete overview
- **COMPLETE_SETUP_GUIDE.md** - Firebase setup details
- **QUICK_START.md** - Command reference
- **STEP_BY_STEP_GUIDE.md** - Phase-by-phase guide
- **BUILD_SUMMARY.md** - What was built
- **FILE_INVENTORY.md** - All files created
- **IMPLEMENTATION_PLAN.md** - Architecture

---

## 🎓 LEARNING RESOURCES

Inside the code you'll find:
- Comments explaining complex logic
- FirebaseManager for database operations
- Adapter pattern for RecyclerViews
- Activity lifecycle management
- Material Design implementation
- Proper error handling
- User authentication best practices

---

## 💡 PRO TIPS

1. **Use Firebase Console to verify data**
   - Check if users are created
   - Verify products are saved
   - Monitor cart items

2. **Check Logcat for debugging**
   - Android Studio → Logcat (bottom panel)
   - Filter by "ERROR" or your app name
   - Most issues show here first

3. **Test incrementally**
   - Test one feature at a time
   - Don't try everything at once
   - Use test accounts

4. **Keep sample data**
   - Create 5-10 products
   - Create 3-5 test users
   - Use for testing features

5. **Backup Firestore rules**
   - Save your security rules
   - Update for production
   - Test before deploying

---

## ✨ YOU'RE READY!

Everything is set up:
- ✅ All code written
- ✅ All layouts created
- ✅ Firebase configured
- ✅ Dependencies added
- ✅ Documentation provided

**Just build and run!**

```bash
cd c:\Users\rayen\AndroidStudioProjects\computershop
./gradlew clean build
# Then click Run in Android Studio
```

---

## 🆘 STUCK?

1. Read the error message carefully
2. Check logcat in Android Studio
3. Verify Firebase console shows created resources
4. Review COMPLETE_SETUP_GUIDE.md troubleshooting
5. Check that google-services.json is in app/ folder
6. Try: File → Invalidate Caches and Restart

---

**Happy Building! 🚀**

