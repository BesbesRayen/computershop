# 🔧 FIX API KEY ERROR - STEP BY STEP

## The Problem
Error: "API key not valid" when trying to register or login.

This means Firebase cannot authenticate with your credentials.

---

## ✅ SOLUTION 1: Verify google-services.json

### Check File Content

Your `google-services.json` should contain REAL values, not placeholders like:
```
❌ WRONG: "AIzaSyDummyApiKeyForTestingPurposes12345"
✅ RIGHT: "AIzaSyD_YourRealLongApiKey_abcdefghij"
```

### Get Real File from Firebase

**Step 1:** Go to Firebase Console
```
https://console.firebase.google.com/
```

**Step 2:** Select your project
```
Click on your TIJARA project
```

**Step 3:** Go to Project Settings
```
Click ⚙️ (Settings icon) → Project Settings
```

**Step 4:** Find Android App Section
```
Scroll down to "Your apps"
Look for: Android icon with "com.example.computershop"
```

**Step 5:** Download google-services.json
```
You should see a button: "Download google-services.json"
Click it
File downloads to Downloads folder
```

**Step 6:** Verify It's Real
```
Open Downloads/google-services.json with Notepad
Check that it has:
- Real project_number (10+ digits)
- Real project_id (your project name)
- Real mobilesdk_app_id
- Real api_key (long string starting with AIzaSy...)
NOT placeholder values
```

**Step 7:** Replace in Project
```
1. Delete: c:\Users\rayen\AndroidStudioProjects\computershop\app\google-services.json
2. Copy real file from Downloads to app/ folder
3. Verify it's in correct location:
   c:\Users\rayen\AndroidStudioProjects\computershop\app\google-services.json
```

**Step 8:** Rebuild
```bash
./gradlew clean build
```

---

## ✅ SOLUTION 2: Check Firebase Configuration

### Step 1: Verify Authentication is Enabled
```
1. Firebase Console → Authentication
2. You should see "Email/Password" provider
3. It should say "Enabled" (not disabled)
4. If not enabled, click Email/Password → Toggle Enable
```

### Step 2: Verify Firestore Database Exists
```
1. Firebase Console → Firestore Database
2. You should see your database created
3. Check collections exist: Internaute, Article, Panier
4. If not created, create them now
```

### Step 3: Verify Android App is Registered
```
1. Firebase Console → Project Settings
2. Scroll to "Your apps"
3. You should see Android icon with "com.example.computershop"
4. If not, click "Add app" and register it
```

---

## ✅ SOLUTION 3: Check SHA-1 Fingerprint

Firebase requires SHA-1 fingerprint for security.

### Step 1: Generate SHA-1
```bash
cd c:\Users\rayen\AndroidStudioProjects\computershop
./gradlew signingReport
```

### Step 2: Copy SHA-1 from Output
```
Look for line like:
SHA-1: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
Copy the entire SHA-1 value
```

### Step 3: Add to Firebase
```
1. Firebase Console → Project Settings
2. Scroll to Android app section
3. Look for "SHA certificate fingerprints"
4. Click "Add fingerprint"
5. Paste your SHA-1
6. Click "Save"
```

---

## ✅ SOLUTION 4: Clear Cache and Rebuild

Sometimes Android caches old files.

```bash
# Clear Gradle cache
./gradlew clean

# Rebuild fresh
./gradlew build

# Clear app cache in emulator (if using)
# Settings → Apps → TIJARA → Clear Cache → Clear Data
```

---

## ✅ SOLUTION 5: Check App Build Configuration

### Verify app/build.gradle.kts

Should have Google Services plugin:
```gradle
// At TOP of file
plugins {
    id 'com.android.application'
    id 'com.google.gms.google-services'  // ← MUST be here
}

// In dependencies
dependencies {
    // Firebase
    implementation platform('com.google.firebase:firebase-bom:32.7.0')
    implementation 'com.google.firebase:firebase-auth'
    implementation 'com.google.firebase:firebase-firestore'
}
```

---

## 🐛 COMMON MISTAKES

### ❌ Mistake 1: Wrong File Location
```
❌ WRONG: c:\Users\rayen\AndroidStudioProjects\computershop\app\src\google-services.json
❌ WRONG: c:\Users\rayen\AndroidStudioProjects\computershop\google-services.json
✅ RIGHT: c:\Users\rayen\AndroidStudioProjects\computershop\app\google-services.json
```

### ❌ Mistake 2: Corrupted File
```
If you manually created the file, it might be incomplete.
Always download from Firebase Console, never create manually.
```

### ❌ Mistake 3: Firebase Project Not Created
```
Make sure you created a Firebase project first.
Go to https://console.firebase.google.com/
Click "Create project"
Wait for it to complete (2-3 minutes)
```

### ❌ Mistake 4: Android App Not Registered
```
Firebase needs to know about your Android app.
Project Settings → Your apps → Add Android app
Package name MUST be: com.example.computershop
```

### ❌ Mistake 5: Email/Password Not Enabled
```
Authentication → Email/Password
Must say "Enabled" (blue toggle)
If disabled, click to enable
```

---

## 📋 VERIFICATION CHECKLIST

Complete this checklist before trying again:

- [ ] Firebase project created and active
- [ ] Android app registered in Firebase (com.example.computershop)
- [ ] Email/Password authentication ENABLED
- [ ] Firestore Database created
- [ ] Collections created: Internaute, Article, Panier
- [ ] Real google-services.json downloaded from Firebase
- [ ] google-services.json placed in app/ folder (NOT app/src/)
- [ ] google-services.json contains real API key (not placeholder)
- [ ] SHA-1 fingerprint added to Firebase
- [ ] app/build.gradle.kts has Google Services plugin
- [ ] Run: ./gradlew clean build (succeeds)
- [ ] App rebuilt successfully

---

## 🚀 AFTER VERIFICATION

Once all checked:

```bash
# Clean and rebuild
./gradlew clean build

# Run app
# Click Run button in Android Studio

# Test registration
# Click Register
# Fill form
# Click Register

# Should see: "Registration successful!"
```

---

## 🆘 IF STILL FAILING

If you still get "API key not valid" error after all steps:

### Option A: Create New Firebase Project
```
1. Delete old project from Firebase Console
2. Create new project: TIJARA
3. Follow setup steps again
4. Download new google-services.json
```

### Option B: Debug Mode
```bash
# Get detailed error logs
./gradlew clean build --debug

# Or run with more info
./gradlew clean build --info
```

### Option C: Check Emulator Settings
```
If using Android Emulator:
1. Open emulator
2. Settings → Apps → Permissions
3. Make sure INTERNET permission is granted
4. Check WiFi is enabled/simulated
```

---

## 📱 EXPECTED SUCCESS

When everything works correctly:

✅ App launches without crash
✅ Login screen appears
✅ Click "Register"
✅ Registration form appears
✅ Fill form and click "Register"
✅ See: "Registration successful!"
✅ Redirected to Login screen
✅ Can login and see Product List

---

**Follow these steps carefully. The "API key not valid" error almost always means your google-services.json is fake or wrong. Download the REAL one from Firebase and it will work! 🚀**

