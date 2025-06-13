package dev.wortel.meshok.mapper;

import dev.wortel.meshok.dto.ItemCreateDto;
import dev.wortel.meshok.dto.ItemDisplayDto;
import entity.Item;
import dev.wortel.meshok.helper.PictureHelper;
import org.mapstruct.*;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel =  SPRING)
public interface ItemMapper {
    @Mapping(target = "pictures", expression = "java(pictureHelper.allFiles(item))")
    ItemDisplayDto toItemDisplayDto(Item item, @Context PictureHelper pictureHelper);

    @Mapping(target = "pictures", expression = "java(pictureHelper.allCreatedFiles(count, id))")
    ItemDisplayDto fromCreateToItemDisplayDto(ItemCreateDto item, int count, long id, @Context PictureHelper pictureHelper);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemStatus", constant = "ACTIVE")
    @Mapping(target = "source", constant = "APPLICATION")
    Item toEntity(ItemCreateDto dto);
}