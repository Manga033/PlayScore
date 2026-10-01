# PlayScore

**An Android app for keeping score during games, with a live scoreboard and a full score history.**

![Kotlin](https://img.shields.io/badge/Kotlin-Android-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-4285F4?logo=jetpackcompose&logoColor=white)
![Room](https://img.shields.io/badge/Database-Room-3DDC84)
![Hilt](https://img.shields.io/badge/DI-Hilt-orange)

PlayScore replaces paper scorecards. Create a game session, add the players or teams, change scores with a tap and watch the scoreboard update in real time. Everything is stored locally on the device, so it works offline.

Built as a university mobile development project (International Burch University), delivered in three assignments from March to May 2026.

## Screenshots

| Home | Create game | Live scoreboard |
|------|-------------|-----------------|
| ![Home](docs/screenshots/home.png) | ![Create game](docs/screenshots/create-game.png) | ![Scoreboard](docs/screenshots/scoreboard.png) |

## Features

- **Accounts:** register and log in with form validation (valid email, password of at least 6 characters, matching confirmation).
- **Game types:** Football, Basketball, Tennis, Board Game and a general Sports type.
- **Create a game:** name the game, pick a type and add players or teams. Sports games require at least two players.
- **Live scoreboard:** increase or decrease any player's score with one tap.
- **Score history:** every score change is logged, so there are no disputes about who scored what.
- **Home screen:** quick access to recent games and game types, with empty-state messages and a scroll-to-top button.
- **Secure passwords:** passwords are hashed with PBKDF2-HMAC-SHA256 and a random salt per user. Accounts created before hashing existed are upgraded automatically on their next login.

## Tech stack

| Area | Technology |
|------|------------|
| Language | Kotlin |
| UI | Jetpack Compose, Material 3 |
| Navigation | Navigation Compose |
| Architecture | MVVM (Compose screens, ViewModels, repositories) |
| Dependency injection | Hilt |
| Local storage | Room (users, games, players, game types, score history) |
| Async | Kotlin coroutines |
| Testing | JUnit 4 |

## Architecture

```
app/src/main/java/com/example/playscore/
├── presentation/
│   ├── navigation/      # Screen routes and the navigation graph
│   ├── theme/           # Colors, typography, Material theme
│   ├── ui/              # Composable screens and reusable components
│   ├── view_model/      # One ViewModel, UI state and navigation event per screen
│   └── util/            # Input validation
└── model/
    ├── data/local/      # Room database, DAOs and entities
    ├── repository/      # Repository interfaces and implementations
    ├── security/        # Password hashing
    ├── domain/          # Domain models (Game, Player, score log entries)
    └── di/              # Hilt modules
```

Screens never talk to the database directly. Each screen observes a `UiState` exposed by its ViewModel, and ViewModels call repositories, which wrap the Room DAOs.

## Getting started

**Requirements:** Android Studio (recent stable version, its bundled JDK is fine) and an Android device or emulator running Android 8.0 (API 26) or higher.

```bash
git clone https://github.com/Manga033/PlayScore.git
```

1. Open the project folder in Android Studio and let Gradle sync.
2. Select an emulator or connected device.
3. Press **Run**.

## Running the tests

```bash
./gradlew test
```

The unit tests cover input validation and password hashing.

## What I learned

- Structuring an Android app with MVVM and unidirectional data flow in Jetpack Compose.
- Modelling relational data (games, players, score history) with Room.
- Wiring dependencies with Hilt instead of constructing objects by hand.
- Why passwords should never be stored in plain text, and how to migrate existing accounts safely.

## Roadmap

- Statistics and charts across finished games
- Cloud backup so scores survive a reinstall

## Author

**Danin Mangafić** – IT student at International Burch University, Sarajevo
[LinkedIn](https://www.linkedin.com/in/danin-mangafi%C4%87-45b39b431) · [GitHub](https://github.com/Manga033)
