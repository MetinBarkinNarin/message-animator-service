package tr.com.example.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import reactor.core.Exceptions;

import java.util.LinkedHashMap;

final class ObjectMapperUtil {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    static <T> T readObject(String line, Class<T> tClass) {
        try {
            return objectMapper.readValue(line, tClass);
        } catch (JsonProcessingException ex) {
            throw Exceptions.propagate(ex);
        }
    }

    static LinkedHashMap<?, ?> objectToMap(Object obj) {
        try {
            return readObject(objectMapper.writeValueAsString(obj), LinkedHashMap.class);
        } catch (JsonProcessingException e) {
            throw Exceptions.propagate(e);
        }
    }
    static String writeToString(Object obj){
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw Exceptions.propagate(e);
        }
    }

}
