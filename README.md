# Calmi 🧘

Calmi is a minimalist Android application designed to help you relax and focus by listening to a variety of soothing sounds. Whether you need to concentrate on work, meditate, or simply unwind, Calmi provides a simple and beautiful interface to create your perfect soundscape.

## ✨ Features

*   **Sound Library:** A collection of high-quality sounds to choose from.
*   **Playback Control:** Play, pause, and mix multiple sounds simultaneously.
*   **Sleep Timer:** Set a timer to automatically stop the audio playback after a certain duration.
*   **Modern Interface:** A clean and intuitive user interface built with Jetpack Compose.
*   **Background Playback:** Listen to sounds even when the app is in the background.

## 🚀 Tech Stack & Architecture

Calmi is built using modern Android development tools and best practices.

*   **Tech Stack:**
    *   **Kotlin:** Primary programming language.
    *   **Jetpack Compose:** For building the UI declaratively.
    *   **Coroutines & Flow:** For asynchronous operations and managing state.
    *   **Hilt:** For dependency injection.
    *   **Jetpack Navigation:** For navigating between screens.
    *   **Media3 ExoPlayer:** For robust and efficient audio playback.
    *   **Coil:** For loading and displaying images.

*   **Architecture:**
    *   **MVVM (Model-View-ViewModel):** To separate UI logic from business logic.
    *   **Clean Architecture:** Follows principles of separation of concerns, with layers for UI, domain, and data.
    *   **Service-based background audio:** Uses a `Service` to handle audio playback, ensuring it continues outside the app UI.

## 📂 Project Structure

The project is structured into packages based on features and layers:

```
com.calmi.app
├── domain        # Core business logic and models (e.g., Sound)
├── di            # Hilt dependency injection modules
├── player        # Service and manager for handling audio playback
└── ui            # Jetpack Compose UI components, screens, and navigation
    ├── home
    ├── player
    └── splash
```

## 🛠️ How to Build

1.  Clone the repository.
2.  Open the project in Android Studio.
3.  Build the project using Gradle: `./gradlew assembleDebug`

## 🔭 Future Scopes

We have many ideas for making Calmi even better!

*   **More Sounds:** Expanding the sound library with new categories and ambiances.
*   **Custom Mixes:** Allowing users to save their favorite sound combinations.
*   **User Accounts:** Syncing favorites and settings across devices.
*   **Advanced Theming:** Adding more themes, including a pure black (AMOLED) mode.

## 🙌 How to Contribute

Contributions are welcome! If you'd like to help improve Calmi, please follow these simple guidelines:

1.  **Fork & Clone:** Fork the repository and clone it locally.
2.  **Create a Branch:** Create a new branch for your feature or bug fix.
    ```bash
    git checkout -b feature/your-awesome-feature
    ```
3.  **Commit Changes:** Make your changes and commit them with a clear and descriptive message.
4.  **Push to Fork:** Push your changes to your forked repository.
5.  **Submit a Pull Request:** Open a pull request to the `main` branch of the original repository.

Please make sure your code adheres to the existing style and that your changes are well-tested.
