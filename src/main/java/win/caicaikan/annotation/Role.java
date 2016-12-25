package win.caicaikan.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import win.caicaikan.constant.RoleType;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Role {
	RoleType[] value() default { RoleType.ADMIN, RoleType.USER };
}
