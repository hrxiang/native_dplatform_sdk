package org.dplatform.sdk;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class DataModel implements Serializable {
    private String action;

    private DataModel() {
    }

    public DataModel(String action) {
        this.action = NullCheck.nonNull(action, "action is null!");
    }

    public String getAction() {
        return action;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> map = new HashMap<>();
        try {
            Class<?> clazz = getClass();
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(this);
                map.put(fieldName, value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }
}
