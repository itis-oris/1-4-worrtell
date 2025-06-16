package dev.wortel.meshok.controller;

import dev.wortel.meshok.task.SaveTask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/migrate")
@RequiredArgsConstructor
@Slf4j
public class MigrationController {
    private final SaveTask saveTask;

    @PostMapping("/start")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<Map<String, Object>> startMigration() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String initiatedBy = auth.getName();

        log.info("Migration started by admin: {}", initiatedBy);

        saveTask.saveAllPrimary();

        return ResponseEntity.ok(new HashMap<>());
    }
}
