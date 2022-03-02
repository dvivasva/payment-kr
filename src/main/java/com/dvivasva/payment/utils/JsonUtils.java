package com.dvivasva.payment.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.List;

public class JsonUtils {

    /**
     * @param json          .
     * @param typeReference .
     * @param <T>           .
     * @return convert JSON into List of Objects.
     * @throws IOException .
     */
    public static <T> List<T> convertFromJsonToList(String json, TypeReference<List<T>> typeReference) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, typeReference);
    }

    /**
     * @param json   .
     * @param aClass .
     * @param <T>    .
     * @return Generic Type Safe Method – convert JSON into Object .
     * @throws IOException .
     */
    public static <T> T convertFromJsonToObject(String json, Class<T> aClass) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(json, aClass);
    }

    /**
     * @param object .
     * @return convert Object into JSON .
     * @throws JsonProcessingException .
     */
    public static String convertFromObjectToJson(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
