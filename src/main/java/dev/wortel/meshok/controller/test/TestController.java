package dev.wortel.meshok.controller.test;

import dev.wortel.meshok.service.MeshokService;
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

    @GetMapping("/run")
    public String testRun() {
        saveTask.saveAllPrimary();
        return "Task executed manually";
    }

    @GetMapping("/api")
    public String testApi() {
        try {
            System.out.println(meshokAPI.getAccountInfo());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return "Task executed manually";
    }
}