# Changelog

All notable changes to this project are documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.1.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Released]

## [2.0.0] - 2026-05-05

### Added
- Native Windows 11 file picker (PowerShell `System.Windows.Forms.OpenFileDialog`),
  with `java.awt.FileDialog` as a fallback when PowerShell is unavailable.
- Click-to-seek on the progress bar, with a hand cursor over the bar.
- Multi-selection in the playlist (Ctrl-/Shift-click) for batch remove and move
  up/down — contiguous blocks move as a unit, non-contiguous selections move
  independently when not blocked by another selected item.
- 3-state repeat button (no repeat / repeat all / repeat one) and 2-state
  shuffle button, each with a distinct programmatic icon.
- GitHub Actions release workflow: pushing a `v*` tag builds a Windows
  `app-image` via `jpackage`, zips it, and publishes a GitHub Release.

### Changed
- Single-instance guard rewritten as `InstanceLock`, using a loopback
  `ServerSocket` instead of a periodically-refreshed temp file. No more 20-second
  wait to relaunch after a crash, and no background timer.
- All button icons are now generated at runtime with `Graphics2D` (Unicode
  symbols rendered to `BufferedImage`s) — no more PNG files for buttons.
- `Images` icon-key constants renamed from PNG filenames (`Play64.png`, …) to
  `btn-xxx` pattern, matching the new programmatic-icon model.
- Player panel layout: playback buttons centered with flexible gaps, transport
  group (`prev / play / stop / next`) visually separated from mode group
  (`repeat / shuffle`) by a 20 px gap, content vertically centered.
- Playlist panel layout: action buttons centered, with a 20 px separator between
  `add / remove / clear` and `up / down`. Removed an incorrect
  `setPreferredSize(285, 23)` that caused the buttons row to overflow.
- Playback buttons resized from 80×65 to 65×65 (square) — better proportion with
  the 48 px symbol icons.
- Song label centered above the progress bar.
- Progress bar now uses the audio-data byte length (computed by parsing the
  ID3v2 header from the file), keeping max and value in the same byte units so
  the bar reaches 100% at end-of-song and click-to-seek lands accurately.

### Fixed
- `IOException: Resetting to invalid mark` when opening MP3 files with large
  ID3v2 tags (embedded artwork, lyrics): `BufferedBasicPlayer` overrides
  `BasicPlayer.initAudioInputStream(File)` to use a `ByteArrayInputStream`,
  which supports unlimited mark/reset. Seek still works because `m_dataSource`
  remains the original `File`.
- Progress bar capping at &lt; 100% for files with large ID3v2 tags
  (`mp3.length.bytes` reports the file size, but `mp3.position.byte` only counts
  audio frame bytes — fixed by computing audio length from the ID3v2 header).
- Seek click position no longer overshoots the visual click point on files with
  ID3v2 tags.

### Removed
- `Control.java` — replaced by `InstanceLock`.
- 8 unused PNG icon files: `Pause64.png`, `Play64.png`, `Skip-backward64.png`,
  `Skip-forward64.png`, `Stop64.png`, `Plus16.png`, `Less16.png`, `Close16.png`.

[Unreleased]: https://github.com/pegasus1992/ReproductorMusica/compare/v1.0.0...HEAD
[1.0.0]: https://github.com/pegasus1992/ReproductorMusica/releases/tag/v1.0.0
