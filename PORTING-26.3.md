# Minecraft 26.3 Fabric port

Based on upstream `26.1-architectury`, commit
`ef10225d7710b97f21844fa18736a6b0e00c710f` (Immersive Snow Reloaded 2.2.0,
Minecraft 26.1.2), the newest Minecraft-version branch available when this port
was made. This branch builds Fabric for Minecraft 26.3 and Java 25. The existing
NeoForge sources remain available but are excluded from the default build;
NeoForge has not been ported or validated here.

## Build

Run with a Java 25 JDK:

```sh
./gradlew :fabric:build
```

The installable artifact is
`fabric/build/libs/immersive_snow_reloaded-fabric-2.2.0-port.1+26.3.jar`.
Install Fabric API for Minecraft 26.3 alongside it. Fabric Loader 0.19.3 or newer
is required; runtime checks used 0.19.5. The source JAR is not an installable mod.

The common compatibility code now compiles against Fabric dependencies and
Fabric API 0.161.0+26.3. Serene Seasons and GlitchCore use exact Modrinth release
IDs because their version strings are shared across loaders:

- `V9PxJPuw`: Serene Seasons Fabric for 26.3, version 26.1.2.0.7.
- `aaUghyGp`: GlitchCore Fabric for 26.3, version 26.3.0.0.3.

Snow Real Magic 26.0.3+fabric and Kiwi 26.0.4+fabric are compile-only API
references. Their published 26.1 runtime JARs are not included or loaded by the
development run. A compatible 26.3 runtime build is needed to use that optional
integration. No dependencies are shaded into the release, and the MIT license
is included. The common code and its Architectury platform transformations are
included in the installable JAR.

## Runtime verification

`qa/server_smoke.py` launches the production JAR in a fresh localhost-only world
using Minecraft 26.3 and checksum-verified cached server/loader libraries. Run it
with a directory containing only the selected dependency JARs:

```sh
python3 qa/server_smoke.py --jar fabric/build/libs/immersive_snow_reloaded-fabric-2.2.0-port.1+26.3.jar --mods /path/to/dependencies --java /path/to/jdk25/bin/java
```

The harness records dependency hashes, logs, and a JSON result under
`build/qa-run/`. It starts the server, force-loads 25 chunks, exercises
`snow reload`, `snow forget`, and `snow recalculate all`, lets the queue run,
reloads data, then saves and stops cleanly. If Serene Seasons is installed it
also switches to mid-winter and mid-summer.

Passed on 2026-09-29:

- Clean Gradle build, including Java compilation and Architectury transformation.
- Standalone server with Fabric API; startup, chunk processing, snow commands,
  resource reload, clean exit, and no logged errors.
- Same server with the exact Serene Seasons and GlitchCore releases above;
  additionally both season-setting commands completed without errors.
- Combined server with Snow Real Magic `26.0.3-port.2+26.3+fabric`, Kiwi
  `26.0.20-port.1+26.3+fabric`, Serene Seasons, GlitchCore, and Fabric API;
  the same chunk, snow-command, winter/summer, reload, and shutdown checks.
- JAR ZIP integrity, mod metadata, license inclusion, and identical SHA-256
  between the runtime-tested JAR and a subsequent clean offline rebuild.

Combined run evidence: `build/qa-run/20260929-123758-122228/` includes
`console.log`, `result.json`, and `launch-audit.json`. All result flags passed,
with process exit code 0 and no logged errors. Exact input SHA-256 values:

- Immersive Snow Reloaded: `d2a374e2742abe4308f563ba1130adb3dfb7ea498507d97d468eb9e195ecf783`.
- Snow Real Magic port.2: `17e41b92efabddcc8fe6add46bcbba1f0557962370afbb43d7758ae8805b3785`.
- Kiwi port.1: `f74a5e157159d1533077bedc3b2b96fb139a086c987874c28e23f3e8df98a867`.

These are automated dedicated-server smoke checks. They do not establish
visual correctness, interactive client behavior, or exhaustive snow/melting
block-state correctness across every biome and configuration.
