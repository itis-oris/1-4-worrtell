package dev.wortel.meshok.mapper;

import dev.wortel.meshok.dto.ItemDto;
import dev.wortel.meshok.entity.Item;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.service.MeshokService;
import org.mapstruct.*;
import static org.mapstruct.MappingConstants.ComponentModel.SPRING;

@Mapper(componentModel =  SPRING)
public interface ItemMapper {
    @Mapping(target = "meshok_id", source = "id")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", expression = "java(meshokService.getFullCategoryName(dto.getCategory()))")
    @Mapping(target = "picturesFolderPath", expression = "java(pictureHelper.folder(dto.getId()))")
    Item toEntity(ItemDto dto, @Context MeshokService meshokService, @Context PictureHelper pictureHelper);
}