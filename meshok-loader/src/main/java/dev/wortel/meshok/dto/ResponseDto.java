package dev.wortel.meshok.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class ResponseDto<T> {
    private int success;
    private Map<String, T> result;
    private String error;
    public boolean isSuccessful() {
        return success == 1;
    }
}