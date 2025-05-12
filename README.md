# Coffee Tracker App

A modern Android application for tracking daily coffee consumption and discovering coffee recipes.

## Features

- Daily coffee consumption tracking
- Daily coffee quotes
- Coffee recipes with filtering (drinks/snacks)
- Dark mode support
- Daily notifications (optional)
- Modern Material Design with glass effect UI

## Requirements

- Android Studio Hedgehog (2023.1.1) or newer
- JDK 17
- Android SDK 34
- Gradle 8.2

## Building in Android Studio

1. Clone the repository:
```bash
git clone https://github.com/XanderCam/Coffee.git
```

2. Open Android Studio and select:
   - File -> Open -> Navigate to the cloned Coffee directory
   - Click "OK" to open the project

3. Wait for the Gradle sync to complete:
   - Android Studio will automatically sync the project with Gradle
   - This may take a few minutes on first open

4. Build the project:
   - Click Build -> Make Project (or press Ctrl+F9/Cmd+F9)
   - Wait for the build to complete

5. Run the app:
   - Select your target device (emulator or physical device)
   - Click Run -> Run 'app' (or press Shift+F10/Ctrl+R)

## Troubleshooting

If you encounter build issues:
1. Ensure you have the correct JDK version (17) selected in Android Studio
   - File -> Project Structure -> SDK Location -> JDK location
2. Update Android Studio and the Android SDK if needed
3. Try File -> Invalidate Caches / Restart
4. Ensure all required SDK components are installed:
   - Tools -> SDK Manager
   - Required: Android SDK Platform 34, Android Build Tools 34.0.0

## Technical Details

- MVVM Architecture
- Room Database for persistence
- Kotlin Coroutines & Flow
- LiveData for reactive UI
- ViewBinding
- Navigation Component
- Material Design Components
- Glide for image loading

## Performance Optimizations

- Memory leak prevention
- Efficient resource management
- RecyclerView optimizations
- Image loading optimizations
- Proper lifecycle management

## License

MIT License

## Author

thaxam.no
