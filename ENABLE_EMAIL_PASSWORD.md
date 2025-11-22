# ENABLE Email/Password Authentication in Firebase Console

## The Error
```
This operation is not allowed. This may be because the given sign-in provider is disabled for this Firebase project.
Enable it in the Firebase console, under the sign-in method tab of the Auth section.
```

## The Fix: Enable Email/Password Sign-in

### Step 1: Go to Firebase Console
Open: https://console.firebase.google.com

### Step 2: Select Your Project
Click on **computershopapp-20109**

### Step 3: Go to Authentication
Click **Authentication** (left sidebar)

### Step 4: Go to Sign-in Method Tab
Click the **Sign-in method** tab (second tab at the top)

### Step 5: Find Email/Password
You should see a list of sign-in providers:
- Google
- Facebook
- Email/Password ← **This one should be ENABLED**
- Phone
- etc.

### Step 6: Enable Email/Password
1. Click on **Email/Password** in the list
2. You should see a toggle or "Enable" button
3. **Make sure it's ENABLED** (toggle should be ON/blue)
4. Click **Save**

### Step 7: Verify
The Email/Password should now show as **Enabled** in the Sign-in method list

## Step 8: Rebuild and Test

1. In Android Studio:
   ```
   Build → Clean Project
   Build → Rebuild Project
   ```

2. Uninstall the app from your device/emulator

3. Run the app again

4. **Try registering** - it should work now!

## Expected Success
After enabling Email/Password, you should see in logcat:
```
Starting user registration for email: rayen@gmail.com
Auth registration successful
User saved to Firestore successfully
Registration successful!
```

## Screenshot Guide
If you go to Firebase Console → Authentication → Sign-in method, it should look like:

```
✓ Email/Password    [Enabled]
  Google           [Disabled]
  Facebook         [Disabled]
  Phone            [Disabled]
```

The checkmark ✓ or "Enabled" status means it's working.

---

**This is the final step! Enable Email/Password in Firebase Console and registration will work!**
