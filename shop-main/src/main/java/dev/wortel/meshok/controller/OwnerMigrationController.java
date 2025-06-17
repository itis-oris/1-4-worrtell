package dev.wortel.meshok.controller;

import dev.wortel.meshok.error.BusinessException;
import dev.wortel.meshok.service.MigrationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
@RequestMapping("/owner/migration")
@PreAuthorize("hasRole('OWNER')")
@Slf4j
@RequiredArgsConstructor
public class OwnerMigrationController {
    private final MigrationService migrationService;

    @PostMapping("/start")
    public String startMigration(RedirectAttributes redirectAttributes) {
        try {
            Map<String, Object> result = migrationService.triggerMigration();

            if (Boolean.TRUE.equals(result.get("success"))) {
                redirectAttributes.addFlashAttribute("successMessage",
                        "Миграция успешно запущена. Обрабатывается: " + result.get("itemsProcessed") + " элементов.");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage",
                        "Не удалось запустить миграцию: " + result.get("error"));
            }
        } catch (Exception e) {
            log.error("Error triggering migration from admin panel", e);
            redirectAttributes.addFlashAttribute("errorMessage",
                    "Произошла ошибка при запуске миграции: " + e.getMessage());
        }
        return "redirect:/owner/migration";
    }

    @GetMapping
    public String start(Model model) {
        model.addAttribute("migrationStatus", "running");
        return "owner/migration";
    }
}