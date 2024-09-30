<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# QuickLink Changelog

## [Unreleased]

## [1.0.11] - 2024-09-30

### Changed

- Now the plugin allows generating links to modified files, even though the lines may be inaccurate
- If the current commit is not reachable from remote branches, the plugin will look for the closest parent commit that is

### Added

- Two new actions: "Copy Current Commit Line Link" (the old behavior) and "Copy Latest Default Line Link"

## [1.0.10] - 2024-06-29

### Added

- Bitbucket URL's support

## [1.0.9] - 2024-06-13

### Fixed

- Fixed multiline selection on IDE version 2024
- Fixed compiling on IDE version 2024

## [1.0.8] - 2024-05-11

### Added

- Add support for generating multiline selection links for GitHub and Gitlab

## [1.0.7]

### Changed

- Merge changes from the 1.9.0 version of the IntelliJ plugin template
- Remove the upper bound of the supported IJ versions
- Now the Copy Line Link action may be called even before the indexes have been built

## [1.0.6]

### Added

- Add support for symlink in file path

## [1.0.5]

### Added

- Add support for git submodule

## [1.0.4]

### Added

- Support Space

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

[Unreleased]: https://github.com/lunakoly/QuickLink/compare/v1.0.11...HEAD
[1.0.11]: https://github.com/lunakoly/QuickLink/compare/v1.0.10...v1.0.11
[1.0.10]: https://github.com/lunakoly/QuickLink/compare/v1.0.9...v1.0.10
[1.0.9]: https://github.com/lunakoly/QuickLink/compare/v1.0.8...v1.0.9
[1.0.8]: https://github.com/lunakoly/QuickLink/compare/v1.0.7...v1.0.8
[1.0.7]: https://github.com/lunakoly/QuickLink/compare/v1.0.6...v1.0.7
[1.0.6]: https://github.com/lunakoly/QuickLink/compare/v1.0.5...v1.0.6
[1.0.5]: https://github.com/lunakoly/QuickLink/compare/v1.0.4...v1.0.5
[1.0.4]: https://github.com/lunakoly/QuickLink/compare/v1.0.3...v1.0.4
[1.0.3]: https://github.com/lunakoly/QuickLink/compare/v1.0.2...v1.0.3
[1.0.2]: https://github.com/lunakoly/QuickLink/compare/v1.0.1...v1.0.2
[1.0.1]: https://github.com/lunakoly/QuickLink/compare/v1.0.0...v1.0.1
[1.0.0]: https://github.com/lunakoly/QuickLink/compare/v0.0.7...v1.0.0
[0.0.7]: https://github.com/lunakoly/QuickLink/compare/v0.0.6...v0.0.7
[0.0.6]: https://github.com/lunakoly/QuickLink/compare/v0.0.5...v0.0.6
[0.0.5]: https://github.com/lunakoly/QuickLink/commits/v0.0.5
