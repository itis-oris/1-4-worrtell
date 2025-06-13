package dev.wortel.meshok.mapper;

import dev.wortel.meshok.dto.ItemDto;
import entity.Item;
import dev.wortel.meshok.helper.PictureHelper;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import dev.wortel.meshok.service.MeshokService;

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
}