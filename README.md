# DREnhanced

DungeonRealms client enhancements for **Minecraft Java 26.3**, **NeoForge 26.3.0.95**, and **Java 25**.
Author: MatthewEDev (.matthewe on Discord).

This is the 2.0.0 port of the Forge 1.12.2 mod. Install NeoForge for Minecraft 26.3, then put
`drenhanced-2.0.0+mc26.3.jar` in that installation's `mods` folder. It runs on the client;
the DungeonRealms server does not need to install it. The old Forge 1.12.2 JAR is incompatible.

## Features and controls

- Coordinates, clue scroll, profession experience/ETA, gem balance, and shard/player-count HUD modules.
- Rarity and clue-scroll inventory outlines, durability percentages, item origins, Shift comparisons,
  weapon DPS estimates, and stat-roll percentages.
- Fishing/mining chat options, experience percentages, classic bossbar/health display, and pause-menu
  buttons for `/logout` and `/hub`.
- **H** opens settings, **Numpad 5** opens the draggable HUD editor, and **G** opens the changelog.
  All three can be rebound in Minecraft's Controls menu.
- `/drenhanced`, `/drenhanced overlay`, `/changelogs`, and `/zone` are local client commands.

Gameplay enhancements activate on `dungeonrealms.net` and its subdomains. For another server address,
use `/drenhanced enable`; that override lasts only until disconnect.

## Settings and stat tables

The mod reads legacy `dungeonrealmsenhanced/module_save.json` if no 26.3 settings exist, then saves to
`dungeonrealmsenhanced/module_save-26.3.json`. The legacy file remains unchanged. HUD enabled states
use the third saved value, independently of their X position. Saves replace the new file atomically
where supported; an unreadable new configuration is copied aside before replacement.

Item data now comes from Minecraft's `minecraft:custom_data` and `minecraft:lore` components.
The mod only reads that data. Actual server compatibility depends on the item metadata the server
or protocol translator supplies to the 26.3 client.

Stat tables are bundled from
[DREnhancedRestful commit 29bee07](https://github.com/dungeonrealmsenhanced/DREnhancedRestful/tree/29bee07ec34f3db6b577b5b8e3e5c72770907b90/values).
They are a versioned snapshot, not a live balance feed. To override them, supply
`dungeonrealmsenhanced/stat-data.json` with the same structure as the bundled resource. The retired
REST information service is not contacted. Weapon DPS retains the original estimated combat multipliers.

## Build and verification

Install JDK 25 and run:

```sh
./gradlew build
```

On Windows, use `gradlew.bat build`. The checked-in wrapper downloads Gradle 9.6.0. Output is in
`build/libs`; use the main JAR, not the `-sources.jar`. GitHub Actions also builds and uploads both.

The regression suite runs with NeoForge's JUnit integration; item-component fixtures use its
ephemeral test server. For an automated client startup, settings migration, and screen-render check:

```sh
./gradlew runClient -PclientSmokeTest
```

That command uses the isolated `run-smoke` directory, writes `smoke-results.txt` and screenshots,
and closes the client when finished. Its development test mod is excluded from the shipped JAR.
See [validation notes](docs/minecraft-26.3-validation.md) for evidence and remaining live-server checks.
