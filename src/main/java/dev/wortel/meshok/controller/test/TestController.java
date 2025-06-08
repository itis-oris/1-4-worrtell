package dev.wortel.meshok.controller.test;

import lombok.RequiredArgsConstructor;
import dev.wortel.meshok.task.SaveTask;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class TestController {

    private final SaveTask saveTask;

    @GetMapping("/test-run")
    public String testRun() {
        saveTask.saveAllPrimary();
        return "Task executed manually";
    }
}