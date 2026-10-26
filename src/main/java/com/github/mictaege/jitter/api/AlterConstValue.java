package com.github.mictaege.jitter.api;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

/**
 * Declares the flavour specific value of a constant of the full model.
 *
 * <p>An {@code @Alter} of a whole class is no longer allowed to swap
 * constants: javac has inlined the full model value into every usage of a
 * constant variable, so the inlined usages would not follow the swap. The
 * flavour variance of a constant's VALUE is declared where the constant
 * lives, with this annotation - one instance per flavour group:
 *
 * <pre><code>
 *     &#64;AlterConstValue(ifActive = Flavour.CUSTOMER_A, with = "customerATable")
 *     &#64;AlterConstValue(ifActive = {Flavour.CUSTOMER_B, Flavour.CUSTOMER_C},
 *                        with = "customerBcTable")
 *     &#64;InlinedConstantApproved
 *     public static final String TABLE =
 *             "com.example.pkg.Constants.TABLE";   // the placeholder anchor
 * </code></pre>
 *
 * <p>The rules, enforced by the build:
 *
 * <ul>
 * <li>String constants are inlined into their usage sites, so their full
 * model value must be the <em>placeholder anchor</em>: the fully qualified
 * name of the constant itself. The placeholder is unique by construction
 * and names the declaring class, so the flavour build can re-bake it to
 * the active flavour's value wherever it was inlined - including
 * annotations of other modules. String constants with this annotation
 * also need {@link InlinedConstantApproved}, because their usages are
 * still inlined and only sound inside annotations.</li>
 * <li>Non-String constants must use the wrapper types ({@code Integer},
 * {@code Boolean}, ...): wrapper constants are no constant variables, so
 * nothing is inlined and every usage is a real reference that follows the
 * flavour at runtime. Primitive types fail the build. Wrapper constants
 * do not need {@link InlinedConstantApproved} - nothing can leak.</li>
 * <li>The annotation must cover every flavour in which the constant is
 * visible (its {@link OnlyIf} gates intersected with the class and
 * package gates), otherwise the build fails.</li>
 * <li>An {@code @Alter} class with constants fails the build - move the
 * constants out or replace the {@code @Alter} with this annotation.</li>
 * </ul>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(FIELD)
@Repeatable(AlterConstValues.class)
public @interface AlterConstValue {

    /** The flavours this value belongs to. */
    String[] ifActive();

    /** The constant's value in those flavours. */
    String with();

}
