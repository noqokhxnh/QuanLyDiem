# QLD Android Project - Context Information

## Project Overview
This is an Android application project named "QLD" (Queensland). The project follows the standard Android project structure and uses Gradle for build management with the modern version catalog approach for dependency management.

## Project Purpose
QLD is envisioned as a comprehensive Queensland lifestyle and services application that provides Queenslanders and visitors with easy access to essential information and services related to the Sunshine State. This could include government services, tourism information, transport updates, emergency services, and local attractions.

## Project Structure
```
QLD/
├── app/                    # Main application module
│   ├── src/
│   │   ├── main/          # Main source code and resources
│   │   │   ├── java/      # Java source files (currently empty)
│   │   │   ├── res/       # Resource files (drawable, values, etc.)
│   │   │   └── AndroidManifest.xml  # Application manifest
│   │   ├── androidTest/   # Instrumentation tests
│   │   └── test/          # Unit tests
├── gradle/                # Gradle wrapper and configuration
│   └── libs.versions.toml # Version catalog for dependencies
├── build.gradle           # Top-level build configuration
├── settings.gradle        # Project settings and module inclusion
├── gradle.properties      # Gradle properties
└── gradlew/gradlew.bat    # Gradle wrapper scripts
```

## Technology Stack
- **Platform**: Android
- **Build System**: Gradle with Android Gradle Plugin (AGP) v8.13.0
- **Language**: Java (based on configuration, though source files are currently empty)
- **UI Framework**: Android XML layouts with Material Design Components
- **Dependency Management**: Gradle Version Catalog (libs.versions.toml)

## Key Configuration Files

### build.gradle (Project Level)
- Uses the plugins block with alias for AGP
- Minimal configuration focused on plugin management

### settings.gradle
- Includes the ':app' module
- Configures repositories for plugin management and dependency resolution
- Uses Google, Maven Central, and Gradle Plugin Portal repositories

### app/build.gradle (Module Level)
- Namespace: com.example.qld
- Compile SDK: 36
- Minimum SDK: 24
- Target SDK: 36
- Dependencies:
  - androidx.appcompat:appcompat
  - com.google.android.material:material
  - junit for unit tests
  - androidx.test dependencies for instrumentation tests

### gradle/libs.versions.toml
- Centralized version management for dependencies
- Defines versions for AGP, JUnit, Espresso, AppCompat, and Material Components
- Uses version references for consistent dependency management

## Building and Running

### Build Commands
```bash
# Make gradlew executable (if needed)
chmod +x gradlew

# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Run tests
./gradlew test

# Install debug APK to connected device
./gradlew installDebug
```

### Development Environment
- Android Studio is the recommended IDE
- Project uses Java 11 compatibility (source and target compatibility)
- AndroidX libraries are enabled
- Non-transitive R classes are enabled for smaller R class size

## Development Conventions
- Uses Material Design Components for UI
- Follows Android best practices for resource management
- Implements proper night mode support (values-night resources)
- Uses centralized version catalog for dependency management
- Follows standard Android project structure

## Current Status
- Project structure is set up but contains no Java source files yet
- Basic resources (colors, themes, strings) are configured
- Manifest is minimal with basic application configuration
- Ready for development to begin by adding activities and functionality

## Adding New Features
1. Create new activities in `app/src/main/java/com/example/qld/`
2. Add corresponding layout files in `app/src/main/res/layout/`
3. Update AndroidManifest.xml if needed for new activities
4. Add any new dependencies to libs.versions.toml if needed
5. Run `./gradlew assembleDebug` to build and test

## Testing
- Unit tests should be placed in `app/src/test/java/com/example/qld/`
- Instrumentation tests should be placed in `app/src/androidTest/java/com/example/qld/`
- Run unit tests with `./gradlew test`
- Run instrumentation tests with `./gradlew connectedAndroidTest`