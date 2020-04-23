package org.dplatform.sdk;

public class NullCheck {

    public static <T> T nonNull(T obj, String msg) {
        if (null == obj) {
            throw new IllegalArgumentException(msg);
        }
        return obj;
    }
}
