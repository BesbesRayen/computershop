# 🔥 ADD FIREBASE CONFIG - STEP BY STEP

## What You Need to Do

You need to add ONE file: `google-services.json`

This file connects your Android app to Firebase.

---

## ✅ STEP 1: Create Firebase Project

### Go to Firebase Console
```
1. Open browser: https://console.firebase.google.com/
2. Click "Add project" button
3. Project name: TIJARA
4. Click "Create project"
5. Wait 2-3 minutes (green loading bar)
```

✅ **Firebase project created!**

---

## ✅ STEP 2: Register Android App

### Add Android App to Firebase

```
1. In Firebase Console, look for Android icon (or "+ Add app")
2. Click Android icon
3. Fill in:
   Package name: com.example.computershop
   App nickname: TIJARA (optional)
4. Click "Register app"
```

✅ **Android app registered!**

---

## ✅ STEP 3: Get SHA-1 Fingerprint (IMPORTANT!)

### Open PowerShell Terminal

```bash
# Navigate to project folder
cd c:\Users\rayen\AndroidStudioProjects\computershop

# Run this command
./gradlew signingReport
```

### In the terminal output, look for:
```
Variant: debug
Config: debug
Store: C:\Users\...\.android\debug.keystore
...
SHA-1: XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX:XX
```

### Copy the SHA-1 value
- Highlight the entire SHA-1 line (starts with XX:XX:...)
- Right-click → Copy

✅ **SHA-1 copied!**

---

## ✅ STEP 4: Enter SHA-1 in Firebase

### Back in Firebase Console

```
1. In the "Android app registration" page
2. Scroll down to "Debug signing certificate SHA-1"
3. Paste your SHA-1 value
4. Click "Next" or "Continue"
```

✅ **SHA-1 registered!**

---

## ✅ STEP 5: Download google-services.json

### Download the File

```
1. Click "Download google-services.json"
2. A file will download to your Downloads folder
3. File name: google-services.json
```

✅ **File downloaded!**

---

## ✅ STEP 6: Place File in Project

### Move the File to App Folder

**Windows Explorer Method:**
```
1. Open your Downloads folder
2. Find: google-services.json
3. Right-click → Copy
4. Navigate to: c:\Users\rayen\AndroidStudioProjects\computershop\app\
5. Right-click → Paste
```

**OR Command Line Method:**
```bash
# Copy from Downloads to app folder
copy %USERPROFILE%\Downloads\google-services.json c:\Users\rayen\AndroidStudioProjects\computershop\app\
```

✅ **File placed in project!**

---

## ✅ STEP 7: Verify File Location

### Check the File is in the Right Place

In Windows Explorer:
```
c:\Users\rayen\AndroidStudioProjects\computershop\
└── app\
    └── google-services.json  ✅ (should be here!)
```

**NOT here:**
```
❌ app\src\google-services.json (WRONG!)
❌ app\src\main\google-services.json (WRONG!)
```

✅ **File in correct location!**

---

## ✅ STEP 8: Sync Gradle

### In Android Studio

```
1. Wait for Android Studio to detect the new file
2. Top menu: File → Sync Now
3. Wait for "Gradle sync finished" message at bottom
4. If no message appears, manually:
   - File → Sync Now
   - OR: Ctrl+Shift+Alt+O
```

✅ **Gradle synced!**

---

## ✅ STEP 9: Enable Firebase Services

### In Firebase Console

#### Enable Authentication
```
1. Left menu: Authentication
2. Click "Get started"
3. Select "Email/Password"
4. Toggle "Enable"
5. Click "Save"
```

#### Create Firestore Database
```
1. Left menu: Firestore Database
2. Click "Create database"
3. Select "Start in test mode"
4. Choose nearest region
5. Click "Enable"
6. Wait 1-2 minutes
```

#### Create Collections (in Firestore)
```
1. Click "Create collection"
2. Collection ID: Internaute
3. Click "Next"
4. Skip adding first document
5. Click "Create"

Repeat for:
- Collection: Article
- Collection: Panier
```

✅ **Firebase services enabled!**

---

## ✅ STEP 10: Build the App

### In Android Studio OR Terminal

**Method 1: Android Studio**
```
1. Top menu: Build → Clean Project
2. Wait for "Clean..." message
3. Top menu: Build → Build Project
4. Wait for "BUILD SUCCESSFUL" at bottom
```

**Method 2: Terminal/PowerShell**
```bash
cd c:\Users\rayen\AndroidStudioProjects\computershop
./gradlew clean build
```

✅ **App built successfully!**

---

## ✅ STEP 11: Run the App

### Method 1: Android Studio (Easiest)
```
1. Click Device Manager (right side panel)
2. If no device: Create new Virtual Device (API 30+)
3. Click Play icon to start emulator
4. Wait 2-3 minutes for Android to boot
5. Click green Run button (▶)
6. Select emulator
7. Click OK
```

### Method 2: Command Line
```bash
./gradlew installDebug
```

### Method 3: Physical Device
```
1. Connect phone via USB
2. Enable Developer Mode + USB Debugging on phone
3. Click Run button
4. Select your device
5. Click OK
```

✅ **App running!**

---

## 🎮 First Run Test

### You should see:
1. **Login Screen** - App starts here
2. Email & Password fields
3. "Register" link
4. Login button

✅ **If you see this, setup is complete!**

---

## 🐛 Troubleshooting

### Problem: "google-services.json not found"

**Solution:**
```
1. Check file is in: app/ folder (NOT app/src/)
2. File name must be exactly: google-services.json
3. Sync Gradle: File → Sync Now
4. Try building again
```

### Problem: "Build fails"

**Solution:**
```bash
./gradlew clean
./gradlew build --refresh-dependencies
```

### Problem: "App won't launch"

**Solution:**
1. Check logcat for errors (bottom of Android Studio)
2. Ensure Firebase collections were created
3. Try restarting Android Studio

### Problem: "Authentication failed"

**Solution:**
1. Verify Email/Password is enabled in Firebase
2. Check internet connection
3. Try registering new account

---

## ✅ Verification Checklist

- [ ] Firebase project created
- [ ] Android app registered in Firebase
- [ ] SHA-1 fingerprint added
- [ ] google-services.json downloaded
- [ ] google-services.json in `app/` folder (NOT app/src/)
- [ ] Gradle synced
- [ ] Authentication enabled in Firebase
- [ ] Firestore database created
- [ ] Collections created (Internaute, Article, Panier)
- [ ] App builds successfully
- [ ] App runs on emulator/device
- [ ] Login screen appears

**If all checked: ✅ You're done!**

---

## 📞 Need Help?

### Check These Files for More Details:
- **COMPLETE_SETUP_GUIDE.md** - Detailed Firebase setup
- **BUILD_AND_RUN.md** - Build & run instructions
- **README.md** - Troubleshooting section
- **STEP_BY_STEP_GUIDE.md** - Phase-by-phase guide

---

## 🎉 YOU'RE READY!

After completing these 11 steps:

✅ Firebase is configured
✅ App can build
✅ App can run
✅ Backend is connected
✅ You can test features

**Next: Register a test account and explore the app!**

---

## 📝 Example Test Flow

```
1. App launches → See Login screen
2. Click "Register"
3. Fill form:
   - Login: testuser
   - Email: test@example.com
   - Password: Test@123
   - Confirm: Test@123
   - Country: Tunisia
4. Click "Register"
5. See "Registration successful!"
6. Redirected to Login
7. Login with test@example.com / Test@123
8. See Product List (empty - add products next)
9. Click "Logout"
10. Back to Login screen

✅ Authentication working!
```

---

**Your TIJARA app is now ready to use! 🚀**

