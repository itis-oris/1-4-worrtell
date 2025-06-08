package dev.wortel.meshok.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleResponseDto<T> {
    private int success;
    private T result;
    private String error;
}