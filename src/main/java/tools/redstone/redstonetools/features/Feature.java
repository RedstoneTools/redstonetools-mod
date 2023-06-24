package tools.redstone.redstonetools.features;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Feature {
    String id() default "";
    String name();
    String description();
    String command();
    boolean worldedit() default false;
}
