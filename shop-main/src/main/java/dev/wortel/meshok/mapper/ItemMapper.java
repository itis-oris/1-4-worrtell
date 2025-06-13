package dev.wortel.meshok.mapper;

import dev.wortel.meshok.dto.ItemCreateDto;
import dev.wortel.meshok.dto.ItemDisplayDto;
import dev.wortel.meshok.dto.ItemDto;
import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.service.MeshokService;
import org.mapstruct.*;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel =  SPRING)
public interface ItemMapper {
    @Mapping(target = "meshokId", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", expression = "java(meshokService.getFullCategoryName(dto.getCategory()))")
    @Mapping(target = "picturesFolderPath", expression = "java(pictureHelper.folder(dto.getId()))")
    @Mapping(target = "itemStatus", constant = "ACTIVE")
    @Mapping(target = "source", constant = "MESHOK")
    Item toEntity(ItemDto dto, @Context MeshokService meshokService, @Context PictureHelper pictureHelper);

    @Mapping(target = "pictures", expression = "java(pictureHelper.allFiles(item))")
    ItemDisplayDto toItemDisplayDto(Item item, @Context PictureHelper pictureHelper);

    @Mapping(target = "pictures", expression = "java(pictureHelper.allCreatedFiles(count, id))")
    ItemDisplayDto fromCreateToItemDisplayDto(ItemCreateDto item, int count, long id, @Context PictureHelper pictureHelper);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "itemStatus", constant = "ACTIVE")
    @Mapping(target = "source", constant = "APPLICATION")
    Item toEntity(ItemCreateDto dto);
}