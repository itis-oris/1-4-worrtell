package dev.wortel.meshok.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

@Component
public class JsonUtil {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper()
            .findAndRegisterModules();

    public static <T> T read(Reader reader, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(reader, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String write(Object object) {
        try {
            return OBJECT_MAPPER.writeValueAsString(object);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T read(String json, TypeReference<T> typeReference) {
        try {
            return OBJECT_MAPPER.readValue(new StringReader(json), typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
