package xyz.vaith.weeblogbackend.enumerate.util;

import xyz.vaith.weeblogbackend.enumerate.BaseEnum;

public class EnumUtils {
    public static <T extends Enum<?> & BaseEnum> T codeOf(Class<T> enumClass, int code) {
        T[] enumConstants = enumClass.getEnumConstants();
        for (T t : enumConstants) {
            if (t.getCode() == code) {
                return t;
            }
        }
        return null;
    }
}
