<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# QuickLink Changelog

## [Unreleased]
## [1.0.3]

### Added

- Support for multiple newer IJ versions

## [1.0.2]

### Added

- Support for 213 versions

## [1.0.1]

### Fixed

- "No project file in GoLand" issue

### Added

- More info on the settings tab usage

## [1.0.0]

### Changed
- Added the 📃 emoji to the GitHub Action's commits
- Removed the `https://` protocol from generated links. Some corporate domains may not support it.

### Added
- Support for custom domains mappings

### Fixed
- A typo in "Locally modified files"

## [0.0.7]
### Added
- Support for 212 versions

## [0.0.6]
### Added
- Plugin Icon
- Gitlab URL's support

### Changed
- Added more info to the description

## [0.0.5]
### Added
- Initial scaffold created from [IntelliJ Platform Plugin Template](https://github.com/JetBrains/intellij-platform-plugin-template)
- Added support for individual commits

### Changed
- Changed the `pluginName` and the `<name>` values to `'Quick Link'
- Reviewed error messages according to the guidelines
- Removed the word `intellij` from everywhere
- Now the plugin generates line links to particular commits only
- Notification balloon timeout reduced to 3 seconds

### Fixed
- Forbade generating links for locally changed files
- A type in `UnsupportedUrlFormatException`
- Plugin ID must now be 17287, not 17286
