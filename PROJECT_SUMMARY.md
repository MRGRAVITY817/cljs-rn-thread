# ClojureScript Expo Project Summary

## Project Overview

This is a **ClojureScript React Native mobile application** built with Expo for cross-platform development. The project demonstrates a social media feed interface with posts, interactions, and modern React Native patterns implemented in ClojureScript using the UIx library for functional components.

## Architecture

### Core Technologies
- **ClojureScript** (v1.11.60) - Primary language compiled to JavaScript
- **Expo** (v52.0.24) - React Native development platform and toolkit
- **UIx** (v1.4.3) - Functional React components and hooks library for ClojureScript
- **Shadow-CLJS** (v2.28.19) - ClojureScript build tool and development environment
- **React Native** (0.76.5) - Cross-platform mobile framework

### Build System
- **shadow-cljs.edn**: Configures React Native target with source maps and development preloads
- **deps.edn**: Manages Clojure/ClojureScript dependencies
- **package.json**: JavaScript dependencies and npm scripts

## Project Structure

```
├── src/
│   ├── app/
│   │   ├── core.cljs           # Main app entry point and root component
│   │   ├── data.cljs           # Mock data for feed posts
│   │   └── feed.cljs           # Feed screen and compose functionality
│   └── components/
│       ├── feed_header.cljs    # Header component for feed
│       ├── feed_item.cljs      # Individual post item component
│       └── flat_list.cljs      # Custom FlatList wrapper
├── dev/
│   └── preload.cljs           # Development build notifications
├── app/                       # Compiled JavaScript output (generated)
├── deps.edn                   # Clojure dependencies
├── shadow-cljs.edn            # Build configuration
├── package.json               # JavaScript dependencies and scripts
└── app.json                   # Expo configuration
```

## Key Files and Components

### Application Core
- **`src/app/core.cljs`** - Root component with counter example and app initialization
  - `(defui counter ...)` - Simple counter with local state
  - `(defui root ...)` - Main app wrapper
  - `(defn init [])` - App initialization using `expo/registerRootComponent`

### Feed Interface
- **`src/app/feed.cljs`** - Social media feed implementation
  - `(defui feed ...)` - Main feed display using FlatList with profile navigation
  - `(defui compose-button ...)` - Floating action button for posting
  - `(defui feed-screen ...)` - Screen wrapper with header and feed

### User Profiles
- **`src/app/profile.cljs`** - User profile screen implementation
  - `(defui profile-screen ...)` - Main profile screen with user details
  - `(defui profile-header ...)` - Header with back navigation, user info, stats
  - `(defui stat-item ...)` - Follower/following count display components

### Reusable Components
- **`src/components/feed_item.cljs`** - Post display with interactions
  - `(defn format-number [n] ...)` - Utility for formatting large numbers
  - `(defui action-button ...)` - Like/comment/share buttons
  - `(defui feed-item ...)` - Complete post item with avatar, content, actions

- **`src/components/feed_header.cljs`** - Navigation header
- **`src/components/flat_list.cljs`** - Custom FlatList wrapper for posts

### Data Layer
- **`src/app/data.cljs`** - Mock data structure
  - `(def mock-posts ...)` - Sample feed posts with user info, content, metrics
  - `(def users ...)` - Extended user profiles with bio, location, stats

## Development Workflow

### Commands
```bash
npm run dev          # Start development server (Expo + ClojureScript watch)
npm run expo:start   # Start Expo development server only
npm run cljs:dev     # Start ClojureScript watch build only
npm run cljs:release # Build production ClojureScript bundle
```

### Development Features
- **Hot Reloading**: ClojureScript hot reload via shadow-cljs
- **REPL Integration**: Connect to running app for interactive development
- **Build Notifications**: Development feedback through React Native error overlay
- **Source Maps**: Full debugging support with original ClojureScript sources

## UIx Patterns and Conventions

### Component Definition
```clojure
(defui component-name
  [props]
  (let [[state set-state] (uix/use-state initial-value)]
    ($ react-native-component
       {:prop1 value1
        :prop2 value2}
       children)))
```

### State Management
- Use `uix/use-state` for local component state
- React Native components accessed via `rn/` namespace
- JSX-like syntax using `$` macro for element creation

### Namespace Organization
- App logic in `app.*` namespaces
- Reusable components in `components.*` namespaces
- Data/constants in separate namespace (`app.data`)

## API and Function Examples

### UIx Core Functions
```clojure
(uix/use-state initial-value)              ; [state setter] hook
($ rn/view {:style {...}} children)        ; Create React Native elements
(defui component-name [props] ...)         ; Define functional component
```

### React Native Integration
```clojure
[react-native :as rn]                      ; Import React Native components
($ rn/flat-list {:data items               ; FlatList usage
                 :render-item render-fn
                 :key-extractor key-fn})
```

## Extension Points

### Adding New Screens
1. Create new `.cljs` file in `src/app/`
2. Define screen component using `defui`
3. Import and use in main app or navigation

### State Management
- Currently uses local React state via UIx hooks
- Can extend with:
  - Re-frame for global state management
  - React Context via UIx context providers
  - External state libraries

### Data Layer
- Mock data in `app.data` namespace
- Extend with:
  - HTTP client integration
  - Local storage (AsyncStorage)
  - Real-time data (WebSocket)

### Styling
- Inline styles using React Native style objects
- Can extend with:
  - StyleSheet optimization
  - Theme providers
  - CSS-in-JS libraries

### Navigation
- Simple state-based navigation between feed and profile screens
- Profile navigation triggered by avatar clicks in feed items
- Can be extended with:
  - React Navigation for more complex routing
  - Tab-based navigation
  - Stack navigation with history

## Build Configuration Details

### Shadow-CLJS (shadow-cljs.edn)
- Target: `:react-native` for mobile compilation
- Output: `app/` directory for compiled JavaScript
- Init function: `app.core/init`
- Development preloads: `uix.preload` and custom `preload`
- Source maps enabled for debugging

### Dependencies (deps.edn)
- Core Clojure/ClojureScript runtime
- UIx for React integration
- Shadow-CLJS for build tooling
- Source paths: `src` and `dev`

### Expo Configuration (app.json)
- App metadata and platform-specific settings
- Asset management and bundling
- New Architecture enabled for React Native

This project serves as a solid foundation for ClojureScript React Native development with modern tooling and functional programming patterns.