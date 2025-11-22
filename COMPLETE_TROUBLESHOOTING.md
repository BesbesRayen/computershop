# Complete Troubleshooting Guide for "Configuration Not Found" Error

## Changes Made:
1. ✅ Enhanced ComputerShopApp with detailed logging
2. ✅ Improved FirebaseManager error handling
3. ✅ Added Firebase initialization checks to RegisterActivity
4. ✅ Added Firestore persistence configuration
5. ✅ Created FirebaseDebugger utility

## Step 1: Rebuild Project
```
Build → Clean Project
Build → Rebuild Project
```

## Step 2: Check Android Studio Logs
After running the app:
1. Open `Logcat` (View → Tool Windows → Logcat)
2. Filter by: `ComputerShopApp` or `RegisterActivity` or `FirebaseManager`
3. Look for messages like:
   - "Firebase initialized successfully" ✓ (Good)
   - "FirebaseAuth initialization error" ✗ (Bad)
   - "Firestore initialization error" ✗ (Bad)

## Step 3: Verify google-services.json
Location should be: `app/google-services.json` (NOT in root)
```
c:\Users\rayen\AndroidStudioProjects\computershop\app\google-services.json
```

The file must contain:
- `project_id`: "computershopapp-20109"
- `package_name`: "com.example.computershop"
- `current_key`: Your API key

## Step 4: Check Firebase Console
1. Go to https://console.firebase.google.com
2. Select "computershopapp-20109" project
3. Check under "Settings" → "Your apps" → "com.example.computershop"
4. Verify:
   - Package name matches: `com.example.computershop`
   - SHA-1 fingerprint is registered (if required)

## Step 5: Enable Firestore Database
1. In Firebase Console, go to Firestore Database
2. If not created, click "Create Database"
3. Start in **Test Mode** (for development)
4. Set Location to closest region

## Step 6: Configure Firestore Security Rules
Go to Firestore → Rules tab and paste:

**For Development (Testing):**
```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if true;
    }
  }
}
```

**Click Publish**

## Step 7: Common Error Messages

| Error | Cause | Solution |
|-------|-------|----------|
| "Configuration not found" | google-services.json missing or invalid | Verify file location and content |
| "FirebaseAuth is NULL" | Firebase not initialized | Rebuild project, clear cache |
| "Permission denied" | Firestore security rules | Update rules to allow writes |
| "Quota exceeded" | Too many write requests | Check Firestore usage limits |

## Step 8: Test the Fix
1. Clean and rebuild project
2. Run on emulator/device
3. Go to Register screen
4. Check Logcat for initialization messages
5. Try registering with test email: `test@example.com`

## If Still Having Issues:
1. Delete app data: Settings → Apps → ComputerShop → Storage → Clear Data
2. Uninstall app completely
3. Clean project: `Build → Clean Project`
4. Rebuild: `Build → Rebuild Project`
5. Reinstall app

## Files Modified:
- ✅ `ComputerShopApp.java` - Enhanced with logging
- ✅ `FirebaseManager.java` - Better error handling
- ✅ `RegisterActivity.java` - Added Firebase checks
- ✅ `AndroidManifest.xml` - Already updated
- ✅ `app/google-services.json` - Already fixed with real API key
