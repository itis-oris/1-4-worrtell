package dev.wortel.meshok.task;

import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.service.ItemService;
import dev.wortel.meshok.service.MeshokService;
import dev.wortel.meshok.util.MeshokAPI;
import entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SaveTask {
    private final ItemMapper itemMapper;
    private final MeshokService meshokService;
    private final ItemService itemService;
    private final MeshokAPI meshokAPI;

//    @Scheduled(cron = "0 30 12 * * ?")
//    public void saveAll() {
//        LinkedList<Long> l = new LinkedList<>();
//        l.add(334677536L);
//        Item item = meshokService.getItems(l).get(0);
//        System.out.println(item.toString());
//        itemService.saveWithPictures(item);
//    }

//    public void saveAllPrimary() {
//        List<Item> items = meshokService.getNewItems();
////        items.forEach(itemService::saveWithPictures);
//        System.out.println(items);
//        itemService.saveWithPictures(items);
//    }

    @Scheduled(cron = "${load.cron.schedule}")
    public void saveAll() {
        saveAllPrimary();
    }

    public void saveAllPrimary() {
        List<Item> items = meshokService.getNewItems();
        itemService.saveWithPictures(items);
    }
}