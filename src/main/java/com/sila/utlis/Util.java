package com.sila.utlis;

import java.util.function.Consumer;

public class Util {
    public static <T> void setIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}
