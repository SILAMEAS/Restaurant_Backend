package com.sila.share;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Consumer;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Utils {
    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
