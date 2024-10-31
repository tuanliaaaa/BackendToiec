package com.toiec.toiec.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    // Phương thức chuyển đổi JSON thành đối tượng
    public static <T> T fromJson(String jsonString, Class<T> valueType) throws IOException {
        return objectMapper.readValue(jsonString, valueType);
    }
    // Phương thức chuyển đổi JSON thành danh sách đối tượng
    public static <T> List<T> fromJsonList(String jsonString, Class<T> valueType) throws IOException {
        return objectMapper.readValue(jsonString, objectMapper.getTypeFactory().constructCollectionType(List.class, valueType));
    }
    // Phương thức chuyển đổi đối tượng thành JSON
    public static String toJson(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj);
    }
}
