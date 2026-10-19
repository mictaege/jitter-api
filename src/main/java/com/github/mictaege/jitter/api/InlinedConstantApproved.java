package com.github.mictaege.jitter.api;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Marks a flavour-gated compile-time constant of the full model as
 * deliberately inlinable.
 *
 * <p>javac inlines constant variables - {@code final} fields of a
 * primitive or {@code String} type with a constant initializer - into the
 * using code. For a constant with a flavour-variant {@link OnlyIf} gate
 * the baked-in value would therefore not follow the gate: whenever
 * surviving code uses the constant, it keeps the full model value in the
 * flavours where the constant is removed. The flavour stripping cannot
 * see those usages - after the inlining they are plain literals, not
 * references.
 *
 * <p>This annotation asserts that the inlining is flavour-consistent:
 * every usage of the constant sits on a member that is gated with a
 * <em>subset</em> of the constant's {@code @OnlyIf} flavours - the member
 * is therefore removed together with the constant in every flavour where
 * the constant is gone, and no baked-in value outlives its gate. This
 * includes annotations and switch labels of such members, which are
 * re-baked or removed together with their member.
 *
 * <p>Without this annotation the build fails for a flavour-gated
 * constant. On the other hand it is an error to use it on a field that is
 * not a constant variable or that has no flavour-variant {@code @OnlyIf}
 * gate: for a non-inlinable field the flavour stripping sees the usages
 * as real references and verifies them, so no assertion is needed.
 *
 * <p>Example:
 *
 * <pre><code>
 *     &#64;OnlyIf(Flavour.CUSTOMER_B)
 *     &#64;InlinedConstantApproved
 *     public static final String CARRIER = "carrier";
 *
 *     &#64;OnlyIf({Flavour.CUSTOMER_A})
 *     public void someBmxCustomerThing() {
 *         ... CustomerBClass.CARRIER ...   // survives only where CARRIER survives
 *     }
 * </code></pre>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
public @interface InlinedConstantApproved {
}
