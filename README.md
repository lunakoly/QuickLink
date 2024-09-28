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
github.com/gitlab.com/... domains).

Additionally, use `Copy Current Commit Line Link` and `Copy Latest Default Line Link` to obtain
links to exactly the currently checked-out commit or the latest locally known commit of the remote's default branch.
The default action is designed to get "some close" link as the main use case is to quickly refer to a place in code:
if the current commit has not been pushed to the remote, we're looking for a parent commit that has been (by considering
common parents between the current commit and either the currently tracked remote branch or the remote's default branch). 

![Settings Tab Image](https://drive.google.com/uc?export=download&id=1qIt8gfIkYNIt8qfIrSP-jl3lrhzvgYMp)

The extension can currently generate links to remotes powered by GitHub, GitLab, Space and Bitbucket services.

---
Plugin based on the [IntelliJ Platform Plugin Template][template].

[template]: https://github.com/JetBrains/intellij-platform-plugin-template
