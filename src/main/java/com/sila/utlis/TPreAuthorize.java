package com.sila.utlis;

import com.sila.utlis.enums.USER_ROLE;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
public @interface TPreAuthorize {
    USER_ROLE[] value();
}
