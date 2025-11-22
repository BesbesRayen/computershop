# Fix for "Configuration Not Found" Error

## What was fixed:

1. **Created Application Class (ComputerShopApp.java)**
   - Initializes Firebase globally when the app starts
   - Ensures `google-services.json` is properly loaded
   - Initializes FirebaseManager

2. **Updated AndroidManifest.xml**
   - Added `android:name=".ComputerShopApp"` to the application tag
   - This ensures the Application class is used when the app launches

3. **Updated RegisterActivity**
   - Added `FirebaseManager.init()` to onCreate()

## What you need to do:

### Step 1: Clean and Rebuild
```
Build → Clean Project
Build → Rebuild Project
```

### Step 2: Important - Check Firestore Security Rules

This error can also occur if Firestore security rules are blocking writes to the "Internaute" collection.

**Go to Firebase Console:**
1. Open https://console.firebase.google.com
2. Select your project (computershopapp-20109)
3. Go to Firestore Database → Rules
4. Update the rules to allow registration:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow anyone to read/write during development (NOT for production!)
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

**For Production, use more restrictive rules:**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow authenticated users to read/write their own data
    match /Internaute/{userId} {
      allow read, write: if request.auth.uid == userId;
    }
    match /Article/{document=**} {
      allow read: if true;
      allow write: if request.auth.token.role == "admin";
    }
    match /Panier/{document=**} {
      allow read, write: if request.auth.uid != null;
    }
  }
}
```

### Step 3: Test Registration Again
1. Run the app
2. Go to Register
3. Try to register with a new email

## Files Modified:
- ✅ Created: `ComputerShopApp.java`
- ✅ Updated: `AndroidManifest.xml`
- ✅ Updated: `RegisterActivity.java`
- ✅ Already fixed: `app/google-services.json` (with real API key)
