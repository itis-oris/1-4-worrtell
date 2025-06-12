package dev.wortel.meshok.controller.test;

import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.service.ItemService;
import dev.wortel.meshok.service.MeshokService;
import dev.wortel.meshok.service.YandexS3Service;
import dev.wortel.meshok.util.MeshokAPI;
import lombok.RequiredArgsConstructor;
import dev.wortel.meshok.task.SaveTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("test")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final SaveTask saveTask;
    private final MeshokAPI meshokAPI;
    private final YandexS3Service yandexS3Service;;
    private final ItemService itemService;
    private final ItemMapper itemMapper;
    private final PictureHelper pictureHelper;

    @GetMapping("/run")
    public String testRun() {
        saveTask.saveAllPrimary();
        return "Task executed manually";
    }

    @GetMapping("/map")
    public String testMap() {
        return itemMapper.toItemDisplayDto(itemService.getItemByMeshokId(325179466L), pictureHelper).toString();
    }

    @GetMapping("/service")
    public String service() {
        return yandexS3Service.fetchAndUploadToS3(itemService.getItemByMeshokId(325179466L), 1);
    }
}