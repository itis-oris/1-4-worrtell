package dev.wortel.meshok.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CategoryDto {
    Long id;
    String name;
    Long parentId;
}
