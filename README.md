# AluCalc — Android (Kotlin + Jetpack Compose) Frontend

Native Android frontend for AluCalc, matching the Figma design: Register → Login → Home →
New Project (3-step wizard: details → profile → windows) → Report.

## Opening the project
1. Open **Android Studio** (Koala/2024.1 or newer recommended).
2. `File > Open` and select the `AluCalc` folder.
3. Let Gradle sync. Android Studio will auto-generate the gradle wrapper jar if it's missing
   (the `gradle-wrapper.properties` is already included, pointing at Gradle 8.7).

## Connecting to your Spring Boot backend
Set your backend's base URL in `app/build.gradle.kts`:

```kotlin
buildConfigField("String", "BASE_URL", "\"http://10.0.2.2:8080/\"")
```

- `10.0.2.2` is how the **Android emulator** reaches `localhost` on your dev machine.
- For a **physical device** on the same Wi-Fi, use your machine's LAN IP, e.g. `http://192.168.1.20:8080/`.
- For production, point this at your deployed backend, e.g. `https://api.alucalc.com/`.

## API contract expected by this app
See `data/remote/ApiService.kt` — every endpoint the app calls is declared there with the
exact path, method, and JSON body/response shape. This mirrors the contract proposed earlier:

```
POST   /api/auth/register
POST   /api/auth/login
GET    /api/projects
POST   /api/projects
GET    /api/projects/{id}
POST   /api/projects/{id}/windows
DELETE /api/projects/{id}/windows/{windowId}
POST   /api/projects/{id}/calculate
GET    /api/projects/{id}/report
GET    /api/reports
```

**If your actual Spring controllers use different paths or field names**, you only need to
edit two files:
- `data/model/Models.kt` — request/response field names (must match your DTOs' JSON keys)
- `data/remote/ApiService.kt` — endpoint paths

Nothing else in the app (screens, ViewModels, navigation) needs to change.

## Auth
- On login/register success, the JWT token returned by your backend is saved via
  `TokenStore` (Jetpack DataStore) and attached as `Authorization: Bearer <token>` to every
  subsequent request (see `RetrofitClient`'s auth interceptor).
- If your backend uses a different auth scheme (session cookie, API key, etc.), adjust
  `RetrofitClient.authInterceptor`.

## Project structure
```
app/src/main/java/com/alucalc/app/
├── MainActivity.kt
├── navigation/NavGraph.kt        # all screen routing
├── data/
│   ├── model/Models.kt           # DTOs — match these to your Spring Boot DTOs
│   ├── remote/ApiService.kt      # Retrofit endpoint definitions
│   ├── remote/RetrofitClient.kt  # Base URL, auth header, JSON config
│   ├── remote/TokenStore.kt      # Persists JWT across app restarts
│   └── repository/AluRepository.kt
├── viewmodel/
│   ├── AuthViewModel.kt
│   ├── ProjectViewModel.kt       # drives the 3-step wizard
│   └── ViewModelFactory.kt
└── ui/
    ├── theme/                    # colors/typography matched to the Figma design
    ├── components/               # reusable buttons, text fields, step header, bottom nav
    └── screens/                  # one file per screen (Register, Login, Home, NewProject,
                                   # SelectProfile, WindowDetails, Report, ProjectsList,
                                   # ReportsHistory, Settings)
```

## Not yet wired up (left as clear TODOs for you)
- **PDF download / share** on the Report screen (`onDownloadPdf`, `onShare` in `NavGraph.kt`) —
  needs your backend's actual PDF endpoint response type (redirect URL vs binary stream).
- **Project detail screen** when tapping a project in the Projects list — currently a no-op;
  add a route once you decide what that screen should show beyond the wizard.
- **Launcher icon** is a placeholder black square — swap in your real app icon via
  Android Studio's Image Asset tool (`res/drawable/ic_launcher_foreground.xml`).

## Next steps
1. Share your actual Spring Boot controller signatures (or Swagger/OpenAPI JSON) and I'll
   line up `ApiService.kt` and `Models.kt` exactly to your backend.
2. Run the app against your local backend with the emulator (`10.0.2.2`) to test the full
   Register → New Project → Report flow end to end.
