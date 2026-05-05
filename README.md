# Reproductor de Música

A lightweight desktop music player for Windows, written in Java / Swing.
Supports MP3 and OGG Vorbis playback, a clickable progress bar with accurate
seeking, repeat / shuffle modes, and a multi-selection playlist.

## Features

- **MP3** and **OGG Vorbis** playback (via `mp3spi` and `vorbisspi`).
- **Native Windows 11 file picker** (PowerShell `OpenFileDialog`), with the
  legacy `java.awt.FileDialog` as automatic fallback.
- **Click-to-seek** on the progress bar — accurate even for files with large
  ID3v2 tags (embedded album art, lyrics, etc.).
- **Repeat** modes: none / all / one. **Shuffle** mode with history tracking.
- **Multi-selection playlist** (`Ctrl` + click, `Shift` + click) — remove or
  reorder several songs at once. Contiguous blocks move as a unit.
- **Single-instance guard** via a loopback `ServerSocket`. No stale lock files
  if the app crashes; the OS releases the port automatically.
- **Programmatic icons** — buttons render Unicode symbols at runtime through
  `Graphics2D`, so the bundle ships without any PNG assets for controls.

## Requirements

- Windows 10 or 11.
- **End users**: no Java install needed — the release bundle includes its own
  runtime.
- **Building from source**: JDK 21 (or newer) and Maven 3.6+.

## Installation

Download `MusicPlayer-<version>.zip` from the
[Releases page](../../releases), unzip it, and run
`MusicPlayer/MusicPlayer.exe`.

## Build from source

```bash
mvn clean package
mvn dependency:copy-dependencies -DoutputDirectory=target/dependency
cp target/reproductor-musica-*.jar target/dependency/

"$JAVA_HOME/bin/jpackage" \
  --type app-image \
  --name MusicPlayer \
  --app-version 2.0.0 \
  --vendor Avuuna \
  --input target/dependency \
  --main-jar reproductor-musica-1.0-SNAPSHOT.jar \
  --main-class avuuna.player.MainPlayer \
  --dest target/dist
```

The launcher will be at `target/dist/MusicPlayer/MusicPlayer.exe`. The whole
`MusicPlayer/` folder is portable; zip it to share.

## Releasing

Pushing a `v*` tag triggers `.github/workflows/release.yml`, which builds the
app image, zips it, and publishes a GitHub Release using the matching section
of `CHANGELOG.md` as the release notes.

```bash
git tag v2.0.0
git push origin v2.0.0
```

## Architecture

Standard MVC; the model also plays the Subject role of the Observer pattern.

```
avuuna.player
├── MainPlayer                entry point + single-instance guard
├── InstanceLock              loopback ServerSocket lock
├── controller
│   └── PlayerController
├── model
│   ├── MusicPlayer           playlist, playback state, repeat/shuffle
│   ├── Song                  extends File, holds duration + byte length
│   └── BufferedBasicPlayer   BasicPlayer override that fixes "invalid mark"
├── view
│   ├── View                  base JFrame
│   └── GUIPlayer             main window
└── utils                     Images (programmatic icon factory), Strings, Utils
```

## Dependencies

- [mp3spi](https://github.com/umjammer/mp3spi) 1.9.5.4 — MP3 audio SPI.
- [vorbisspi](https://github.com/umjammer/vorbisspi) 1.0.3.3 — OGG Vorbis SPI.
- [SLF4J](https://www.slf4j.org/) 1.7.36 — logging facade.
- JLayer (bundled, vendored under `javazoom.jl.player.basic`) — MP3 decoder.

## Changelog

See [CHANGELOG.md](CHANGELOG.md).
