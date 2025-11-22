# Fix "CONFIGURATION_NOT_FOUND" - reCAPTCHA Error

## The Problem
Firebase Auth requires reCAPTCHA to be properly configured for Android. The error `CONFIGURATION_NOT_FOUND` means reCAPTCHA isn't set up correctly in your Firebase project.

## Solution: Configure reCAPTCHA in Firebase Console

### Step 1: Go to Firebase Console
1. Open https://console.firebase.google.com
2. Select your project: **computershopapp-20109**

### Step 2: Navigate to Authentication Settings
1. Go to **Authentication** (left sidebar)
2. Click the **Settings** tab (gear icon at top)
3. Scroll down to **reCAPTCHA Enterprise**

### Step 3: Enable reCAPTCHA Enterprise (Recommended)
1. Under "reCAPTCHA Enterprise", click **Enable**
2. Follow the prompts to create a reCAPTCHA key
3. Select "Android" as the platform
4. Add your app package name: `com.example.computershop`

### Step 4: Alternative - Disable reCAPTCHA for Testing
If reCAPTCHA Enterprise setup is too complex, you can:
1. Go to **Authentication** → **Sign-in method**
2. Find and disable any reCAPTCHA protection temporarily
3. This is **NOT recommended for production**

## Step 5: Verify Your google-services.json
Your file has been updated with missing services. Location: `app/google-services.json`

Ensure it contains:
```json
"services": {
  "appinvite_service": { ... },
  "google_signin_service": { ... },
  "firebase_database": { ... },
  "analytics_service": { ... }
}
```

## Step 6: Rebuild and Test
```
Build → Clean Project
Build → Rebuild Project
```

Then run the app and try registering again.

## What Changed in Code:
✅ Updated `google-services.json` with complete services configuration
✅ Added detailed logging in `FirebaseManager.registerUser()`
✅ Better error handling for reCAPTCHA issues

## If Still Getting the Error:
1. Delete the app from your device
2. Clear app data in Firebase Console (optional)
3. Rebuild the entire project
4. Verify reCAPTCHA is enabled in Firebase Console

## Quick Checklist:
- [ ] reCAPTCHA is enabled in Firebase Console
- [ ] google-services.json is at `app/google-services.json`
- [ ] Project rebuilt after changes
- [ ] App reinstalled on device
- [ ] Internet connection is working
