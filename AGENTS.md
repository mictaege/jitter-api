# AGENTS.md — jitter-api

## Project purpose
`jitter-api` contains the public API of _jitter_: the annotations and contracts that application
code uses to mark "flavour"-specific source code (e.g. `@Flavour`-style annotations) so it can be
included/excluded when building different variants of an application from a single source base.
It has no behaviour of its own beyond defining this API — the actual source transformation happens
in [`jitter-plugin`](https://github.com/mictaege/jitter-plugin), which depends on this module and on
[`spoon-gradle-plugin`](https://github.com/mictaege/spoon-gradle-plugin).

Part of the jitter/spoon family: `jitter-api` is the lowest-level, dependency-free module; both
`jitter-plugin` and end-user projects (like `eval.jitter`) depend on it directly.

## Technical stack
- Language: Java (sources under `src/main/java` only — no dedicated test sources in this module).
- Build tool: Gradle (Kotlin DSL, `build.gradle.kts`), `java-library` plugin — build via `./gradlew`.
- Release/publishing: `org.jreleaser`, publishing to Maven Central under group `io.github.mictaege`.
- License: Apache License 2.0.

## Project semantics / domain notes
- This module defines the annotation-based contract consumed by Java Annotation Processing /
  the Spoon-based transformation in `jitter-plugin` — treat any change to annotations here as a
  breaking API change for both `jitter-plugin` and consumer applications.
- Keep this module dependency-free/minimal; it is meant to be a lightweight compile-time API that
  any application can depend on without pulling in the full plugin/transformation machinery.

## Working conventions
- Version changes here must be coordinated with `jitter-plugin`, since it depends on a specific
  `jitter-api` version and re-exposes its annotations to consumers.
- Favor small, additive, backward-compatible API changes; avoid removing or renaming existing
  annotations/methods without updating `jitter-plugin` and `eval.jitter` accordingly.
