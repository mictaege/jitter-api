# AGENTS.md — jitter-api

Project context and conventions live in OpenSpec: `openspec/config.yaml` (project context) and
`openspec/specs/<capability>/spec.md` (capability specs). Use the OpenSpec workflow for behavior
changes: propose via `/openspec-propose`, implement via `/openspec-apply-change`, validate with
`openspec validate`, finish with `/openspec-archive-change`. Family-level planning lives in the
store `jitter` (`~/openspec/jitter`, use `openspec ... --store jitter`).

## Read the relevant spec before changing

- The flavour-markup annotation API (`@OnlyIf`/`@Fork`/`@Alter`/`@AlterConstValue`/
  `@InlinedConstantApproved`/`@Tags`): `openspec/specs/annotations/spec.md`

## Working conventions

- Version changes here must be coordinated with `jitter-plugin`, since it depends on a specific
  `jitter-api` version and re-exposes its annotations to consumers.
- Favor small, additive, backward-compatible API changes; avoid removing or renaming existing
  annotations/methods without updating `jitter-plugin` and `eval.jitter` accordingly.
- Keep this module dependency-free/minimal; build via `./gradlew`.
- Annotation semantics changes are breaking for jitter-plugin and consumers — propose them as
  OpenSpec changes first.
