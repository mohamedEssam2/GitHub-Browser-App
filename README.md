GitHub Browser App
A modern Android application built with Kotlin and Jetpack Compose that allows users to authenticate with GitHub, browse their repositories, and view repository branches with a beautiful, responsive UI.

🚀 Features
Secure GitHub OAuth Authentication

Repository Browsing with pagination and search

Branch Management for each repository

Modern UI with Material Design 3

Pull-to-Refresh functionality

Offline Resilience with proper error handling

Clean Architecture with MVVM pattern

Dependency Injection with Dagger Hilt

Comprehensive Testing strategy

🏗 Architecture
The app follows Clean Architecture with MVVM pattern:

📱 Presentation Layer (Compose UI + ViewModels)
    ↓
🎯 Domain Layer (UseCases + Repository Interfaces)
    ↓
💾 Data Layer (Repository Implementations + APIs + Storage)
Modules Structure
app: Main application module with UI components

data: Data layer with repositories, API clients, and storage

domain: Business logic with use cases and repository interfaces

core: Shared utilities and common components

🛠 Tech Stack
Language: Kotlin 2.0.0+

UI: Jetpack Compose with Material Design 3

Architecture: Clean Architecture + MVVM

DI: Dagger Hilt

Networking: Retrofit + Moshi

Async: Coroutines + Flow

Security: Android KeyStore + Encrypted SharedPreferences

Testing: JUnit, MockK, Turbine

Build: Gradle with Version Catalog

⚡ Quick Start

 Configure GitHub OAuth
Go to GitHub Developer Settings

Create a new OAuth App:

Application name: GitHub Browser

Homepage URL: https://github.com

Authorization callback URL: githubbrowser://auth

Update app/src/main/res/values/github_oauth_config.xml:

xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="github_client_id">YOUR_ACTUAL_CLIENT_ID</string>
    <string name="github_client_secret">YOUR_ACTUAL_CLIENT_SECRET</string>
    <string name="github_redirect_uri">githubbrowser://auth</string>
</resources>


🏃 Running the App
Build the project in Android Studio or via Gradle

Run on device/emulator with API 24+

Sign in with GitHub when prompted

Browse repositories and explore branches


📁 Project Structure
github-browser/
├── app/                          # Main application module
│   ├── src/main/
│   │   ├── kotlin/com/example/githubbrowser/
│   │   │   ├── presentation/     # UI components and ViewModels
│   │   │   │   ├── auth/         # Authentication screens
│   │   │   │   ├── repositories/ # Repository list screens
│   │   │   │   ├── branches/     # Branch list screens
│   │   │   │   └── ui/           # Shared UI components
│   │   │   ├── di/               # Dagger Hilt modules
│   │   │   └── MainActivity.kt
│   │   └── res/                  # Resources
│   └── src/test/                 # Unit tests
│
├── data/                         # Data layer module
│   ├── src/main/
│   │   └── kotlin/com/example/githubbrowser/data/
│   │       ├── remote/           # API interfaces and DTOs
│   │       ├── repository/       # Repository implementations
│   │       ├── storage/          # Local data storage
│   │       └── di/               # Data layer DI modules
│   └── src/test/                 # Data layer tests
│
├── domain/                       # Domain layer module
│   ├── src/main/
│   │   └── kotlin/com/example/githubbrowser/domain/
│   │       ├── model/            # Domain models
│   │       ├── repository/       # Repository interfaces
│   │       ├── usecase/          # Use cases
│   │       └── di/               # Domain layer DI modules
│   └── src/test/                 # Domain layer tests
│
└── core/                         # Core utilities module
    └── src/main/
        └── kotlin/com/example/githubbrowser/core/
            ├── common/           # Shared utilities
            └── extensions/       # Extension functions


🎯 Implementation Details
Authentication Flow
User initiates OAuth flow

GitHub redirects back to app with auth code

App exchanges code for access token

Token securely stored using Android KeyStore

Token used for authenticated API calls

Repository Browsing
Paginated repository list

Real-time search with debouncing

Pull-to-refresh functionality

Repository details with language chips

Branch Management
Branch list for selected repository

Protection status indicators

Commit information display

⚖️ Trade-offs & Decisions
1. Architecture Choices
✅ Clean Architecture with MVVM

Pros: Separation of concerns, testability, maintainability

Cons: Increased boilerplate, steeper learning curve

✅ Single Activity with Navigation Compose

Pros: Simplified navigation, better state management

Cons: Limited deep linking capabilities

2. UI Framework
✅ Jetpack Compose

Pros: Modern declarative UI, less code, better previews

Cons: Relatively new, some libraries not fully compatible

3. Dependency Injection
✅ Dagger Hilt

Pros: Reduced boilerplate, compile-time safety

Cons: Complex setup, longer build times

4. Networking
✅ Retrofit + Moshi

Pros: Type-safe, efficient JSON parsing

Cons: Reflection usage, larger method count

5. Async Programming
✅ Coroutines + Flow

Pros: Structured concurrency, reactive streams

Cons: Learning curve for developers new to coroutines

6. Security
✅ Android KeyStore + Encrypted SharedPreferences

Pros: Hardware-backed security, platform standards

Cons: Complex implementation, device compatibility issues


🚧 Future Enhancements
Short-term (v1.1)
Repository Details Screen

Readme preview

File browser

Issue list

Enhanced Search

Filter by language, stars, forks

Sort options

Offline Support

Room database for caching

Offline repository browsing

Medium-term (v1.2)
Pull Request Management

PR list and details

Code review interface

Notifications

GitHub notifications with badges

Push notifications for important events

Collaboration Features

Organization support

Team management

Long-term (v2.0)
Multi-account Support

Switch between GitHub accounts

Enterprise GitHub support

Git Operations

Basic Git commands (clone, commit, push)

Code editing capabilities

Cross-platform

Compose Multiplatform for iOS

Desktop version


