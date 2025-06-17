package dev.wortel.meshok.service;
import dev.wortel.meshok.dto.CategoryDto;
import dev.wortel.meshok.dto.LotDto;
import entity.Item;
import dev.wortel.meshok.helper.JsonParser;
import dev.wortel.meshok.helper.PathHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import dev.wortel.meshok.mapper.ItemMapper;
import org.springframework.stereotype.Service;
import dev.wortel.meshok.util.MeshokAPI;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeshokService {
    private final MeshokAPI meshokAPI;
    private final JsonParser jsonParser;
    private final ItemMapper itemMapper;
    private final PathHelper pathHelper;
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
        idList.forEach(id -> items.add(itemMapper.toEntity(jsonParser.toItem(meshokAPI.getItemInfo(id.toString())), this, pathHelper)));
        return items;
    }

    private List<LotDto> getAllLots() {
        return new JsonParser().toLotList(meshokAPI.getItemList());
    }

    public List<Item> getNewItems() {
        List<Long> idList = getAllLots().stream().map(LotDto::getId).toList();
        List<Long> newIdList = itemService.filterNonExistingIds(idList);
        List<Long> idsToProcess = newIdList.stream().limit(10).toList();
        log.info("Processing {} new items: {}", idsToProcess.size(), idsToProcess);
        return getItems(idsToProcess);
    }
}