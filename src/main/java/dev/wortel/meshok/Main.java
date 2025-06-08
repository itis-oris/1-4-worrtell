package dev.wortel.meshok;

import lombok.extern.slf4j.Slf4j;
//import org.example.lib.service.UpdateService;
import dev.wortel.meshok.util.MeshokAPI;

@Slf4j
public class Main {
    public static void main(String[] args) {
//        String token = "f3cfb2cb5903df0f48231453f1877108";
//        MeshokAPI api = new MeshokAPI(token);
//        try {
//            System.out.println(api.getAccountInfo());
//            System.out.println(api.getItemInfo("312864470"));
//        }
//        catch (Exception ignored) {}

//        String jsonResponse = api.getItemList();
//        List<Lot> lots = new JsonParser().parse(jsonResponse);
//        System.out.println(lots);

        //System.out.println(api.getItemInfo("315965509"));

//        imageUtil.save("334677536", 2);

//        YandexDiskService diskService = new YandexDiskService("y0__xCG96bWAxjuwzcg_Z7FghNN0TNgJYEPgT9Tb38nxzKYp-DGjw");
//
//        PictureHelper ph = new PictureHelper();
//        System.out.println(ph.path("334677536", 2));
//        System.out.println(ph.newFolder("334677536"));
//        System.out.println(ph.name(2));
//        String publicUrl = diskService.uploadFile(
//                ph.path("334677536", 2),
//                ph.newFolder("334677536"),
//                ph.name(2)
//        );
//        System.out.println(publicUrl);

//        YandexDiskExcelService excelService = new YandexDiskExcelService("y0__xCG96bWAxjuwzcg_Z7FghNN0TNgJYEPgT9Tb38nxzKYp-DGjw");
//
//        Map<String, String> newData = Map.of(
//                "Дата", "2024-03-15",
//                "Показатель", "85%",
//                "Комментарий", "Обновленные данные"
//        );

//        try {
//            excelService.updateExcelFile("test.xlsx", newData);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        //UpdateService updateService = new UpdateService();
//        updateService.update();
//        MeshokService meshokService = new MeshokService(api, new JsonParser());
//        LinkedList<Long> l = new LinkedList<>();
//        l.add(334677536L);
//        ItemDto itemDto = meshokService.getItems(l).get(0);
//        log.info(ItemMapper.INSTANCE.toEntity(itemDto).toString());

        try {
            //log.info(api.getCategoryInfo("14309"));
            //log.info(api.getCategoryInfo("13116"));
//            log.info(api.getCategoryInfo("140"));
//            CategoryDto category = new JsonParser().toCategory(api.getCategoryInfo("14309"));
//            log.info(category.getId().getClass().getName());
            // "parentId": "0"
            //log.info(new MeshokService(api, new JsonParser()).getFullCategoryName(14309L));
            //log.info(api.getItemInfo("334677536"));
        }
        catch (Exception ignored) {}
    }
}
