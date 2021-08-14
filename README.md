# QuickLink

![Build](https://github.com/lunakoly/QuickLink/workflows/Build/badge.svg)
[![Version](https://img.shields.io/jetbrains/plugin/v/17287.svg)](https://plugins.jetbrains.com/plugin/17287)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/17287.svg)](https://plugins.jetbrains.com/plugin/17287)

<!-- Plugin description -->

IntelliJ-based plugin for generating quick links to particular file lines.

<!-- Plugin description end -->

## Installation

- Using IDE built-in plugin system:
  
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "QuickLink"</kbd> >
  <kbd>Install Plugin</kbd>
  
- Manually:

  Download the [latest release](https://github.com/lunakoly/QuickLink/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>

## Usage

Use `Cmd`+`Shift`+`L` on macOS or `Ctrl`+`Shift`+`L` on Windows and Linux to generate a URL-link to the line
denoted by your current cursor location (alternatively, hit double `Shift` and search for the `Copy Line Link` action).
The newly generated link will be copied to your clipboard automatically.

Use the `Settings > Tools > Quick Link` tab to configure custom domain mappings.
It allows associating corporate domains with the corresponding service providers
(link generation would be done in the same way as it's done for the official 
github.com/gitlab.com/... domains)

![Settings Tab Image](https://drive.google.com/uc?export=download&id=1qIt8gfIkYNIt8qfIrSP-jl3lrhzvgYMp)

Unfortunately, the extension
- can only generate links to remotes powered by GitHub or GitLab services now
- can not generate links to locally modified files (even if these modifications take place after the current cursor location)

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
