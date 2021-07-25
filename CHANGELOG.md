<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# QuickLink Changelog

## [Unreleased]
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
