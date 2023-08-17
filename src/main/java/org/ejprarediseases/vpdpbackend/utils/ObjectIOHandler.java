package org.ejprarediseases.vpdpbackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectIOHandler {

    /**
     * Deserialize JSON string into an object of the specified class.
     *
     * @param json             The JSON string to deserialize.
     * @param outputObjectClass The class representing the type of the output object.
     * @param <T>              The type of the output object.
     * @return An object of the specified class, populated with data from the JSON string.
     * @throws JsonProcessingException if there's an error during JSON deserialization.
     */
    public static <T> T deserialize(String json, Class<?> outputObjectClass) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return (T) objectMapper.readValue(json, outputObjectClass);
    }
}
