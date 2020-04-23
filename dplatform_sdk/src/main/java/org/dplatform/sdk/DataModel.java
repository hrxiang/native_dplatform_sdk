package org.dplatform.sdk;

import java.util.HashMap;
import java.util.Map;

public abstract class DataModel {
    private Map<String, Object> map = new HashMap<>();

    DataModel(String action) {
        map.put("action", NullCheck.nonNull(action, "action is null"));
    }

    void put(String key, Object value) {
        map.put(key, value);
    }

    public Map<String, Object> getMap() {
        return map;
    }
}
