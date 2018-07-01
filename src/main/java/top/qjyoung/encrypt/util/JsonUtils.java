package top.qjyoung.encrypt.util;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Json 工具类
 */
public class JsonUtils {
    private static ObjectMapper mapper = new ObjectMapper();
    
    public static String toString(Object obj) {
        return toJson(obj);
    }
    
    public static String toJson(Object obj) {
        try {
            return mapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("序列化对象【" + obj + "】时出错", e);
        }
    }
    
    public static <T> T toBean(Class<T> entityClass, String jsonString) {
        try {
            return mapper.readValue(jsonString, entityClass);
        } catch (Exception e) {
            throw new RuntimeException("JSON【" + jsonString + "】转对象时出错", e);
        }
    }
    
    public static ObjectMapper getMapper() {
        return mapper;
    }
}
