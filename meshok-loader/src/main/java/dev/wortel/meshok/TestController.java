package dev.wortel.meshok;

import dev.wortel.meshok.task.SaveTask;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("test")
@RestController
@RequiredArgsConstructor
public class TestController {

    private final SaveTask saveTask;

    @GetMapping("/run")
    public String testRun() {
        saveTask.saveAllPrimary();
        return "Task executed manually";
    }

    @GetMapping
    public String test() {
        return "Hey";
    }
}