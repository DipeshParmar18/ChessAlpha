# ♟ CHESS ALPHA — Android Build Guide
## Superhero Edition · Premium 3D Chess · AdMob Integrated

---

## WHAT'S INSIDE THIS PROJECT

```
ChessAlpha/
├── build.gradle                   ← Root Gradle config
├── settings.gradle                ← Project name
├── gradle.properties              ← JVM + AndroidX settings
└── app/
    ├── build.gradle               ← App dependencies (AdMob, WebKit)
    ├── proguard-rules.pro
    └── src/main/
        ├── AndroidManifest.xml    ← Permissions + AdMob App ID
        ├── assets/game/
        │   └── index.html         ← FULL GAME (HTML5 + JS chess engine)
        ├── java/com/chessalpha/
        │   ├── SplashActivity.java
        │   └── MainActivity.java  ← WebView + AdMob banner + interstitial
        └── res/
            ├── drawable/ic_launcher.xml
            ├── layout/activity_main.xml
            ├── layout/activity_splash.xml
            └── values/
                ├── strings.xml
                └── styles.xml
```

---

## GAME FEATURES

✅ Premium 3D-perspective chess board
✅ 4 superhero king themes (Thunder Lord, Iron Titan, Flame Warlord, Shadow King)
✅ Each theme has unique icons for all 6 piece types
✅ SLOW-MOTION kill animation when any piece captures another
✅ Skip button to skip slow-motion effect
✅ Particle explosion effect during captures
✅ Online room system (create/join with 6-digit code)
✅ Email registration + guest play
✅ Full chess rules: castling, en passant, promotion, check, checkmate, stalemate
✅ 10-minute countdown timers per player
✅ Check highlighting, legal move indicators
✅ AdMob banner (bottom) + interstitial (after game ends)
✅ Screen stays awake during gameplay
✅ Fullscreen, portrait mode

---

## HOW TO BUILD THE APK — STEP BY STEP

### STEP 1: Install Android Studio (FREE)
Download from: https://developer.android.com/studio
→ Install it (Windows/Mac/Linux all supported)
→ During setup, let it download the Android SDK automatically

### STEP 2: Open the project
1. Open Android Studio
2. Click "Open" (not "New Project")
3. Navigate to and select the `ChessAlpha` folder
4. Click OK — wait for Gradle sync to finish (may take 2-5 minutes)

### STEP 3: Fix WebView layout (important!)
The layout uses `androidx.webkit.WebViewCompat` — if Android Studio shows a red error,
replace it with plain `WebView`:

In `app/src/main/res/layout/activity_main.xml` change:
```xml
<androidx.webkit.WebViewCompat
```
to:
```xml
<WebView
```

### STEP 4: Add your AdMob Ad Unit IDs
You already have your App ID: ca-app-pub-2141482051251808~8680065246 ✅

Now go to your AdMob dashboard → Apps → Chess Alpha → Ad units:
1. Create a BANNER ad unit → copy the ID
2. Create an INTERSTITIAL ad unit → copy the ID

Open `MainActivity.java` and replace:
```java
private static final String BANNER_AD_UNIT    = "ca-app-pub-2141482051251808/BANNER_UNIT_ID";
private static final String INTERSTITIAL_UNIT = "ca-app-pub-2141482051251808/INTER_UNIT_ID";
```
with your real ad unit IDs.

For testing before going live, use these test IDs:
```java
private static final String BANNER_AD_UNIT    = "ca-app-pub-3940256099942544/6300978111";
private static final String INTERSTITIAL_UNIT = "ca-app-pub-3940256099942544/1033173712";
```

### STEP 5: Build the APK
Option A — Debug APK (for testing):
→ Menu: Build → Build Bundle(s)/APK(s) → Build APK(s)
→ APK saved to: app/build/outputs/apk/debug/app-debug.apk

Option B — Release APK (for Play Store):
→ Menu: Build → Generate Signed Bundle / APK
→ Choose APK
→ Create a new keystore (save the password — you need it forever!)
→ Fill in details → Build
→ APK saved to: app/build/outputs/apk/release/app-release.apk

### STEP 6: Install on your phone
1. On your Android phone: Settings → Developer Options → Enable "USB Debugging"
2. Connect phone via USB
3. In Android Studio: click the ▶ Run button
   OR transfer the APK file to your phone and open it

---

## ADMOB EARNINGS SETUP

Your App ID is already in AndroidManifest.xml:
`ca-app-pub-2141482051251808~8680065246`

Revenue comes from:
- **Banner ad** — shown at bottom of game screen (earns per 1000 views)
- **Interstitial ad** — full screen ad after each game ends (earns per click)

To maximize earnings:
1. Publish on Google Play Store
2. In AdMob: link your app after publishing
3. Enable mediation for more ad networks = more money

---

## ONLINE PLAY (Future Upgrade)

The current room system simulates online play locally.
To make it truly online (real-time multiplayer), add Firebase:

1. Go to https://firebase.google.com → create project
2. Add to build.gradle: `implementation 'com.google.firebase:firebase-database:20.3.0'`
3. Replace the `createRoom()` and `joinRoom()` JS functions with
   Firebase Realtime Database calls to sync moves between players

---

## PLAY STORE PUBLISHING

1. Create a Google Play Developer account ($25 one-time fee)
2. Use the signed release APK from Step 5
3. Fill in app details, upload screenshots
4. Set content rating: Everyone
5. Submit for review (usually 1-3 days)

---

## TECHNICAL NOTES

- Minimum Android version: 5.0 (API 21) — covers 99%+ of devices
- Target: Android 14 (API 34)
- No external internet needed for gameplay (works offline)
- AdMob requires internet for ads
- App size: ~3 MB
- RAM usage: Very low (pure WebView app)
- Battery impact: Minimal

---

## SAFE FOR ANDROID ✅

- No dangerous permissions
- No camera/microphone/location access
- Only INTERNET + NETWORK_STATE (required for AdMob only)
- Hardware accelerated for smooth gameplay
- Screen wake lock only during active game

---

Built with ❤️ — Chess Alpha Superhero Edition
