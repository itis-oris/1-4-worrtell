package dev.wortel.meshok.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import dev.wortel.meshok.dto.*;
import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.exception.ApiException;
import dev.wortel.meshok.util.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@Data
public class JsonParser {

    public List<LotDto> toLotList(String jsonResponse) {
        ResponseDto<LotDto> response = JsonUtil.read(jsonResponse, new TypeReference<>() {});

        if (!response.isSuccessful()) {
            throw new ApiException("API error: " + response.getError());
        }

        return new ArrayList<>(response.getResult().values());
    }

    public List<Item> toItemList(String jsonResponse) {
        ResponseDto<Item> response = JsonUtil.read(jsonResponse, new TypeReference<>() {});
        return new ArrayList<>(response.getResult().values());
    }

    public CategoryDto toCategory(String jsonResponse) {
        try {
            SingleResponseDto<CategoryDto> response = JsonUtil.read(
                    jsonResponse,
                    new TypeReference<>() {}
            );

            if (response.getResult() == null) {
                throw new ApiException("Category data is empty");
            }

            return response.getResult();

        } catch (ApiException e) {
            System.out.println("here toCategory");
            throw new ApiException(e.getMessage());
        }
    }

    public ItemDto toItem(String jsonResponse) {
        try {
            SingleResponseDto<ItemDto> response = JsonUtil.read(
                    jsonResponse,
                    new TypeReference<>() {}
            );

            if (response.getResult() == null) {
                throw new ApiException("Item data is empty");
            }

            return response.getResult();

        } catch (ApiException e) {
            throw new ApiException(e.getMessage());
        }
    }
}

