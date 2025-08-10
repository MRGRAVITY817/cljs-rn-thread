# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Development Commands

### Primary Development Workflow
- `npm run dev` - Start development server (runs Expo and ClojureScript watch builds concurrently)
- `npm run expo:start` - Start Expo development server only
- `npm run cljs:dev` - Start ClojureScript watch build only
- `npm run cljs:release` - Build production ClojureScript bundle

### Package Management
- `npm install` or `yarn` - Install dependencies

## Architecture Overview

This is a **ClojureScript React Native project** built with Expo, using UIx (React hooks for ClojureScript) as the primary UI library.

### Key Technologies
- **ClojureScript**: Primary language using shadow-cljs as the build tool
- **Expo**: React Native development platform for cross-platform mobile apps
- **UIx**: Functional React components and hooks library for ClojureScript
- **React Native**: Mobile app framework

### Project Structure
```
├── src/app/          # Main application source code
│   └── core.cljs     # Root component and app initialization
├── dev/              # Development-specific code
│   └── preload.cljs  # Development utilities and build notifications
├── app/              # Compiled ClojureScript output (generated)
├── deps.edn          # Clojure dependencies
├── shadow-cljs.edn   # ClojureScript build configuration
└── package.json      # Node.js dependencies and scripts
```

### Core Components Architecture

**Main Entry Point**: `src/app/core.cljs`
- Defines the root React Native component using UIx
- Contains a simple counter example with local state
- Uses `expo/registerRootComponent` for app initialization

**Development Setup**: `dev/preload.cljs`
- Handles build error notifications in development
- Configures React Native dev settings for ClojureScript workflow

### Build Configuration

**shadow-cljs.edn** configures:
- `:react-native` target for mobile compilation
- Source map generation for debugging
- Development preloads for hot reloading
- Warning-as-errors for strict compilation

**Key UIx Patterns**:
- Use `defui` for functional components
- Use `uix/use-state` for local state management
- Use `$` macro for JSX-like element creation
- React Native components are accessed via `rn/` namespace

**Component Usage Rules**:
- **ALWAYS use `components.flat-list/flat-list`** instead of `rn/FlatList` for all list components
- Import custom components from `src/components/` when they exist
- Prefer project-specific components over raw React Native components

**React Native Component Naming**:
- **ALWAYS use PascalCase for React Native components**: `rn/TouchableOpacity`, `rn/View`, `rn/Text`, `rn/Image`
- **NEVER use kebab-case or camelCase**: Incorrect: `rn/touchable-opacity`, `rn/touchableOpacity`
- **Common correct patterns**:
  - `rn/View` (not `rn/view`)
  - `rn/Text` (not `rn/text`) 
  - `rn/TouchableOpacity` (not `rn/touchable-opacity`)
  - `rn/SafeAreaView` (not `rn/safe-area-view`)
  - `rn/ScrollView` (not `rn/scroll-view`)
  - `rn/TextInput` (not `rn/text-input`)
  - `rn/FlatList` (not `rn/flat-list`)
  - `rn/Image` (not `rn/image`)

**SafeAreaView Requirements for Screen Headers**:
- **ALWAYS wrap screen headers with SafeAreaView**: Prevents conflicts with phone notches and status bars
- **SafeAreaView must be the root element** of header components: `($ rn/SafeAreaView {...} ($ rn/View {...} content))`
- **NEVER use View as root element** for headers: Incorrect: `($ rn/View {...} header-content)`
- **Proper SafeAreaView pattern**:
  ```clojure
  (defui my-header
    [{:keys [props]}]
    ($ rn/SafeAreaView {:style {:background-color "white"
                                :border-bottom-width 0.5
                                :border-bottom-color "#e1e8ed"}}
       ($ rn/View {:style {:flex-direction "row"
                           :align-items "center"
                           :padding-horizontal 16
                           :padding-vertical 12}}
          ;; header content here
          )))
  ```
- **Apply to all screen headers**: Navigation headers, modal headers, screen titles, etc.

**UIx Import Requirements**:
- **ALWAYS include `defui` in `:refer` when using `defui` macro**: `[uix.core :as uix :refer [$ defui]]`
- **ALWAYS include `$` in `:refer` when creating React elements**: Required for JSX-like syntax
- **Common UIx imports pattern**: `[uix.core :as uix :refer [$ defui]]` for components
- **For hooks only**: `[uix.core :as uix]` then use `uix/use-state`, `uix/use-effect`, etc.
- **Never use `defui` without importing it** - this will cause compilation errors

**Function and Component Definition Rules**:
- **ALWAYS place docstrings before parameter vectors**: `(defn my-fn "docstring" [params] body)`
- **NEVER place docstrings after parameter vectors**: Incorrect: `(defn my-fn [params] "docstring" body)`
- **Correct defn format**: `(defn function-name "Optional docstring" [parameters] body)`
- **Correct defui format**: `(defui component-name "Optional docstring" [props] body)`
- **Multi-line definitions should align properly**:
  ```clojure
  (defn function-name
    "Optional docstring explaining what this function does"
    [parameters]
    body)
  ```

### State Management
The project uses React hooks (via UIx) for local state management. For complex state, consider:
- UIx context providers
- Re-frame (if adding)
- Integrant (for dependency injection)

### Hot Reloading
- ClojureScript provides hot reloading via shadow-cljs
- React Native's Fast Refresh is disabled in favor of ClojureScript reloading
- Build notifications are forwarded to React Native's error overlay

### Testing
No test framework is currently configured. Consider adding:
- shadow-cljs test runner for unit tests
- Expo testing tools for integration tests

## Common Development Tasks

### Adding New Screens/Components
1. Create new ClojureScript files in `src/app/`
2. Use `defui` macro for components
3. Import React Native components via `[react-native :as rn]`
4. Use UIx hooks for state management

### Adding Dependencies
- **Clojure/ClojureScript**: Add to `deps.edn` under `:deps`
- **JavaScript/React Native**: Add to `package.json` and run `npm install`

### Debugging
- Use shadow-cljs REPL: `clojure -M -m shadow.cljs.devtools.cli cljs-repl app`
- Inspect via React Native debugger
- Console logs appear in Expo development tools

### Building for Production
1. Run `npm run cljs:release` to compile optimized ClojureScript
2. Use Expo build tools for app packaging: `expo build:android` or `expo build:ios`

## File Structure Notes

- All source paths are configured in `deps.edn` under `:paths`
- Compiled JS output goes to `app/` directory
- React Native entry point is `app/index.js` (generated by shadow-cljs)
- Expo configuration is in `app.json`

## Development Workflow Rules

### Auto-Commit Policy
When making changes to files in this repository, automatically commit the changes to git with descriptive commit messages. This includes:

- **Source code changes** - New components, features, or bug fixes
- **Configuration updates** - Changes to build files, dependencies, or settings
- **Documentation updates** - README, CLAUDE.md, or other markdown files
- **New features** - Complete implementations of app functionality

**Commit Message Format**:
```
Brief description of changes

- Bullet point describing specific changes made
- Additional details about implementation
- Any breaking changes or important notes

🤖 Generated with [Claude Code](https://claude.ai/code)

Co-Authored-By: Claude <noreply@anthropic.com>
```

**Exception**: Do not auto-commit if the user specifically requests to review changes first or explicitly asks not to commit.