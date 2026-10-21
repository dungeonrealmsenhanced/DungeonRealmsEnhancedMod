# Minecraft 26.3 port validation

Target verified on October 21, 2026: Mojang's version manifest lists **26.3** as the latest stable
release; 26.4-snapshot-5 is a development snapshot. The build pins NeoForge 26.3.0.95,
ModDevGradle 2.0.148, Gradle 9.6.0, and Java 25.

## Automated checks

Final local result: **15/15 JUnit tests passed**, the build and `verifyJar` passed, and **17/17 packaged
client smoke assertions passed**. The smoke test verified the production class was loaded from the
built JAR, then rendered all eleven tested screens. See the [assertion report](validation/client-smoke-results.txt),
[settings screenshot](validation/settings.png), and [HUD editor screenshot](validation/hud-editor.png).

Tested JAR: `drenhanced-2.0.0+mc26.3.jar`.
SHA-256: `25107322322e0dbd5cf996412715f0bb174ead3780112cda5cfc6cf24cf6e1d6`.

- `gradlew.bat build`: compiles the mod, packages metadata/resources and the logging dependency,
  and runs the regression suite.
- JUnit covers saved enable flags, malformed placements, shard footers, settings serialization,
  profession level caps, modern item components, non-mutating reads, numeric modifier/augmentation
  ranges, estimated DPS, malformed modifiers, mythic precedence, bundled stat tables, and rich tooltips.
- `gradlew.bat runClient -PclientSmokeTest`: launches Minecraft 26.3 with the packaged production JAR and a
  separate development test mod. It checks all five active HUD modules, offline stat initialization,
  legacy/new settings migration and reload, unchanged legacy files, and actual rendering of every
  settings category, the HUD editor, and changelog.

Client screenshots and the assertion report are generated under `run-smoke`; none of the test mod
classes or test metadata are included in the distributable. Existing game installations and worlds
are not used by the test.

## Live acceptance still required

No authenticated DungeonRealms session was used. Before calling this a live-server release, verify:

1. The server accepts a 26.3 NeoForge client (directly or through its protocol compatibility layer).
2. Server items retain the expected keys in `minecraft:custom_data`, including rarity, type, modifiers,
   augmentations, origin, durability, level and XP. Check lore, Shift comparison and Ctrl origin display.
3. Real tab footers and bossbar messages populate the correct shard, balance, player count, health and zone.
4. Mining/fishing XP, clue-scroll objectives, drops and dry-streak tracking match actual server messages.
5. HUD placement and inventory outlines behave at the player's GUI scale and with other client mods.

The native HUD/screens replace the old Forge screen subclasses and immediate OpenGL rendering.
The previously unregistered experimental new-lore renderer and CPS click-throttling listener were
removed; the active five-module selection remains the same. `/info1` now displays port/build information
instead of the old testing-only mining simulation. The classic health option draws health near the
hotbar without changing the client's experience level. Brightness uses Minecraft's supported 0–1 range.
