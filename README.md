# Android String Translator [[Download](https://plugins.jetbrains.com/plugin/31747-android-string-translator)]

![Build](https://github.com/SoumikBhatt/plugin-translators/workflows/Build/badge.svg) 

**Android String Translator** is an IntelliJ Platform plugin designed for Android developers to automate the localization of `strings.xml` resources. Translate your app into 100+ languages in seconds using Google Translate or LibreTranslate.

## 🚀 Features

- **Multi-Provider Support**: Choose between Google Translate API (Paid) or LibreTranslate (Free/Self-hosted).
- **One-Click Translation**: Right-click any `strings.xml` file to start the translation flow.
- **Keyboard Shortcut**: Use `Option+T` (macOS) or `Alt+T` (Windows/Linux) for instant translation.
- **Searchable Language Selection**: Quickly find your target language from a list of 100+ supported locales.
- **Placeholder Protection**: Automatically wraps Android placeholders (e.g., `%s`, `%1$s`, `%d`) to ensure they are not corrupted during translation.
- **Automatic Resource Generation**: Generates the correct `values-xx/strings.xml` directory and file structure automatically.

## 🛠 Installation

### From JetBrains Marketplace
- Open **Settings/Preferences** > **Plugins** > **Marketplace**.
- Search for "Android String Translator".
- Click **Install**.

### Manual Installation
1. Download the latest release `.zip` file from the [Releases](https://github.com/SoumikBhatt/plugin-translators/releases) page.
2. In your IDE, go to **Settings/Preferences** > **Plugins**.
3. Click the ⚙️ (Gear Icon) and select **Install Plugin from Disk...**.
4. Select the downloaded `.zip` file and restart the IDE.

## 📖 Usage

1. **Configure Settings**:
   - Go to `Tools -> Android String Translator -> Settings`.
   - Select your provider (Google or LibreTranslate).
   - Enter your API Key (for Google) or Base URL (for LibreTranslate).
2. **Translate**:
   - Locate your default `strings.xml` in the Project view.
   - Right-click and select **Translate Strings** OR press `Alt+T` / `Option+T`.
3. **Select Language**:
   - Pick your target language from the dialog.
4. **Result**:
   - A new localized `strings.xml` will be created in the appropriate `values-xx` folder.

## 🏗 Building from Source

To build the plugin locally:

```bash
./gradlew buildPlugin
```

The distribution zip will be available in `build/distributions/`.

## 📄 License
This project is licensed under the MIT License.
