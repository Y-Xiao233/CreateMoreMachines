package net.yxiao233.createmoremachines.utils;

import java.util.List;
import java.util.Map;

public class MapUtil {
    public static <T> List<T> keyList(Map<T, ?> map){
        return map.keySet().stream().toList();
    }

    public static <T> List<T> valueList(Map<?, T> map){
        return map.values().stream().toList();
    }
}
