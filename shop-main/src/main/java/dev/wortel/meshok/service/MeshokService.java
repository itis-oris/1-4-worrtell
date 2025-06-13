package dev.wortel.meshok.service;

import entity.Item;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.util.MeshokAPI;
import dev.wortel.meshok.dto.CategoryDto;
import dev.wortel.meshok.dto.LotDto;
import dev.wortel.meshok.helper.JsonParser;
import org.springframework.stereotype.Service;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeshokService {
    private final MeshokAPI meshokAPI;
    private final JsonParser jsonParser;
    private final ItemMapper itemMapper;
    private final PictureHelper pictureHelper;
    private final ItemService itemService;

    private CategoryDto getCategory(Long id) {
        return jsonParser.toCategory(meshokAPI.getCategoryInfo(id.toString()));
    }

    public String getFullCategoryName(Long id) {
        StringBuilder result = new StringBuilder();
        CategoryDto category = getCategory(id);

        result.insert(0, category.getName());

        Long categoryId;
        while ((categoryId = category.getParentId()) != 0L) {
            category = getCategory(categoryId);
            result.insert(0," ").insert(0,category.getName());
        }

        return result.toString();
    }

    private List<Item> getItems(List<Long> idList) {
        List<Item> items = new LinkedList<>();
        idList.forEach(id -> items.add(itemMapper.toEntity(jsonParser.toItem(meshokAPI.getItemInfo(id.toString())), this, pictureHelper)));
        return items;
    }

//    используется только один раз при первичной загрузке - хотя нет, по сути же бд пустая и можно просто использовать метод подтягивания несуществующих айтемов
//    private List<LotDto> getAllLots() {
//        return new JsonParser().toLotList(meshokAPI.getItemList());
//    }

    private List<LotDto> getAllLots() {
        String mock = """
                {"success":1,"result":{"0":{"id":"325179466","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"588.00"},"1":{"id":"327119340","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"326.00"},"2":{"id":"327197065","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"815.00"},"3":{"id":"327197091","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"815.00"},"4":{"id":"327198985","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"4073.00"},"5":{"id":"327201437","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"119.00"},"6":{"id":"327201919","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"41.00"},"7":{"id":"327203420","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"80.00"},"8":{"id":"333057830","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"700.00"},"9":{"id":"333059158","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"500.00"},"10":{"id":"333061624","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"1200.00"},"11":{"id":"333062255","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"500.00"},"12":{"id":"327204628","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"61.00"},"13":{"id":"315012988","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"17.00"},"14":{"id":"315013590","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"10.00"},"15":{"id":"315032408","saleType":"Sale","quantity":"1","sold":"0","curencyId":"2","price":"31.00"}}}
                """;
        log.info(mock);
        return new JsonParser().toLotList(mock);
    }

    public List<Item> getNewItems() {
        List<Long> idList = getAllLots().stream().map(LotDto::getId).toList();
        List<Long> newIdList = itemService.filterNonExistingIds(idList);
        log.info("New items {}", newIdList);
        return getItems(newIdList);
    }
}