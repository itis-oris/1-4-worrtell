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
    //private final MigrationService migrationService;
    private final SaveTask saveTask;

    @PostMapping("/start")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> startMigration() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String initiatedBy = auth.getName();

        log.info("Migration started by admin: {}", initiatedBy);

        saveTask.saveAllPrimary();

//        try {
//            Map<String, Object> result = migrationService.runMigration(initiatedBy);
//            return ResponseEntity.ok(result);
//        } catch (Exception e) {
//            log.error("Migration failed", e);
//
//            Map<String, Object> errorResponse = new HashMap<>();
//            errorResponse.put("success", false);
//            errorResponse.put("timestamp", LocalDateTime.now());
//            errorResponse.put("error", e.getMessage());
//
//            return ResponseEntity.internalServerError().body(errorResponse);
//        }
        return ResponseEntity.ok(new HashMap<>());
    }

//    @GetMapping("/status")
//    @PreAuthorize("hasRole('ADMIN')")
//    public ResponseEntity<Map<String, Object>> getMigrationStatus() {
//        return ResponseEntity.ok(migrationService.getStatus());
//    }
}
