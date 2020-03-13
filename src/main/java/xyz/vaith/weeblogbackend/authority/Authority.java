package xyz.vaith.weeblogbackend.authority;

import org.springframework.web.bind.annotation.Mapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Mapping
@Documented
public @interface Authority {
    boolean enable() default true;
    String role() default "admin";
}
