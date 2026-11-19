# Jitter Annotations Specification

## Purpose

The public flavour-markup API of `jitter-api` (`com.github.mictaege.jitter.api`): annotations that
mark code/resources as flavour-specific. All are `@Documented`, `@Retention(RUNTIME)`; flavour
names are Strings matching the flavours configured in the Gradle build (apps should use String
constants, e.g. a `Flavour` class). The semantics are consumed by jitter-plugin's stripping.

## Requirements

### Requirement: `@OnlyIf` marks code present only in listed flavours

`@OnlyIf` SHALL target PACKAGE, TYPE, FIELD, CONSTRUCTOR, METHOD and take `String[] value()` —
the flavour names the annotated element belongs to. The element SHALL be removed from every other
active flavour (and kept in the full 150% model).

#### Scenario: Flavour-gated field
- **WHEN** a field is annotated `@OnlyIf(Flavour.CUSTOMER_A)` and the active flavour is CUSTOMER_B
- **THEN** the field is absent from the CUSTOMER_B flavour model and present in CUSTOMER_A

### Requirement: `@Fork` redirects a method per flavour

`@Fork` SHALL target METHOD, be `@Repeatable(Gateway.class)` (container `Gateway`), and take
`ifActive()` (flavour) and `to()` (the method to call instead in that flavour).

#### Scenario: Forked method
- **WHEN** a method is `@Fork(ifActive = "NASA", to = "nasaImpl")` and the active flavour is NASA
- **THEN** calls dispatch to `nasaImpl`; in other flavours the original body runs

### Requirement: `@Alter` swaps a type per flavour

`@Alter` SHALL target TYPE, be `@Repeatable(Alternatives.class)` (container `Alternatives`), and
take `ifActive()` and `with()` (the alternative class used in that flavour). An `@Alter` class
SHALL NOT declare constant variables (rejected — see jitter-plugin's inlined-constants spec).

#### Scenario: Altered class
- **WHEN** a class is `@Alter(ifActive = "ESA", with = EsaImpl.class)` and the active flavour is ESA
- **THEN** the ESA model uses `EsaImpl` in place of the altered class

### Requirement: `@AlterConstValue` sets a constant's value per flavour

`@AlterConstValue` SHALL target FIELD, be `@Repeatable(AlterConstValues.class)`, and take
`String[] ifActive()` plus `String with()` (the value in those flavours). It SHALL work with
wrapper types and String only (primitives are inlined by javac and cannot be altered
post-compile). Coverage of every visible flavour SHALL be verified by the plugin's audit.

#### Scenario: Flavour-variant constant
- **WHEN** a `String` constant is `@AlterConstValue(ifActive = {"ESA"}, with = "esa-endpoint")`
- **THEN** the ESA flavour model and the dejittered sources carry `"esa-endpoint"`

### Requirement: `@InlinedConstantApproved` marks reviewed constants

`@InlinedConstantApproved` SHALL target FIELD with no attributes: the developer's assertion that
every usage of a flavour-variant constant variable sits in code gated with a subset of the
constant's flavours, so the javac-inlined value is harmless. Without the marker, the audit fails
the build for flavour-variant constants.

#### Scenario: Approved gated constant
- **WHEN** a `final String` constant is `@OnlyIf("NASA")` and `@InlinedConstantApproved`, used only
  in `@OnlyIf("NASA")` code
- **THEN** the constant audit passes

### Requirement: `@Tags` attaches free-form tags

`@Tags` SHALL target PACKAGE, TYPE, FIELD, CONSTRUCTOR, METHOD and take `String[] value()` for
free-form classification tags (used e.g. by critical-terms verification).

#### Scenario: Tagged element
- **WHEN** a method is `@Tags({"critical", "billing"})`
- **THEN** the tags are visible to annotation processing / the plugin's critical-terms checks
