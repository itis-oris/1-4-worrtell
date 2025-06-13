package dev.wortel.meshok.controller.test;

import dev.wortel.meshok.entity.Cart;
import dev.wortel.meshok.helper.PictureHelper;
import dev.wortel.meshok.mapper.ItemMapper;
import dev.wortel.meshok.repository.CartRepository;
import dev.wortel.meshok.repository.ItemRepository;
import dev.wortel.meshok.service.ItemService;
import dev.wortel.meshok.service.UserService;
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
    private final CartRepository cartRepository;
    private final UserService userService;
    private final ItemRepository itemRepository;

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

    @GetMapping("/all")
    public String all() {
        return itemService.getItemById(1L).toString();
    }

//    @GetMapping("/cart")
//    public String cart() {
//        Cart cart = new Cart();
//        cart.setUser(userService.findByEmail("1@mail.ru"));
//        cart.setItems(itemRepository.findAll());
//        cartRepository.save(cart);
//        return cartRepository.findAll().toString();
//    }
}