package dev.wortel.meshok.service;//package org.example.lib.service;
//
//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.streaming.SXSSFWorkbook;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import reactor.core.publisher.Mono;
//import java.io.*;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.stream.Stream;
//
//@Service
//public class ExcelExportService {
//
//    private final ItemRepository itemRepository;
//    private final YandexDiskService yandexDiskService;
//    private final ExcelExportHistoryRepository exportHistoryRepository;
//
//    public ExcelExportService(ItemRepository itemRepository,
//                              YandexDiskService yandexDiskService,
//                              ExcelExportHistoryRepository exportHistoryRepository) {
//        this.itemRepository = itemRepository;
//        this.yandexDiskService = yandexDiskService;
//        this.exportHistoryRepository = exportHistoryRepository;
//    }
//
//    @Transactional
//    public Mono<String> exportItemsToExcelWithSync(String folderPath) {
//        return Mono.fromCallable(() -> {
//                    // 1. Создаем потоковую Excel-книгу (хранит только 100 строк в памяти)
//                    SXSSFWorkbook workbook = new SXSSFWorkbook(100);
//
//                    try {
//                        Sheet sheet = workbook.createSheet("Items");
//
//                        // 2. Создаем заголовки
//                        createHeaders(sheet);
//
//                        // 3. Получаем поток данных из БД
//                        try (Stream<Item> itemStream = itemRepository.streamAllItems()) {
//                            AtomicInteger rowNum = new AtomicInteger(1);
//
//                            // 4. Обрабатываем каждую запись
//                            itemStream.forEach(item -> {
//                                // Добавляем строку в Excel
//                                addItemToSheet(sheet, rowNum.getAndIncrement(), item);
//
//                                // Обновляем флаг экспорта в БД
//                                item.setExportedToExcel(true);
//                                itemRepository.save(item);
//                            });
//                        }
//
//                        // 5. Сохраняем во временный файл
//                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//                        workbook.write(outputStream);
//                        return new ByteArrayInputStream(outputStream.toByteArray());
//
//                    } finally {
//                        workbook.dispose(); // Очищаем временные файлы
//                    }
//                })
//                .flatMap(inputStream -> {
//                    // 6. Генерируем имя файла с timestamp
//                    String filename = "items_export_" + System.currentTimeMillis() + ".xlsx";
//
//                    // 7. Загружаем на Яндекс.Диск
//                    return yandexDiskService.uploadFile(inputStream, folderPath, filename)
//                            .flatMap(fileUrl -> {
//                                // 8. Сохраняем информацию о выгрузке в историю
//                                ExcelExportHistory history = new ExcelExportHistory();
//                                history.setExportDate(LocalDateTime.now());
//                                history.setFileName(filename);
//                                history.setFileUrl(fileUrl);
//                                history.setRecordCount(itemRepository.countByExportedToExcel(true));
//
//                                return Mono.fromCallable(() -> {
//                                    exportHistoryRepository.save(history);
//                                    return fileUrl;
//                                });
//                            });
//                });
//    }
//
//    private void createHeaders(Sheet sheet) {
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"ID", "Name", "Description", "Price", "Created At", "Updated At"};
//
//        for (int i = 0; i < headers.length; i++) {
//            Cell cell = headerRow.createCell(i);
//            cell.setCellValue(headers[i]);
//
//            // Стиль для заголовков
//            CellStyle headerStyle = sheet.getWorkbook().createCellStyle();
//            Font headerFont = sheet.getWorkbook().createFont();
//            headerFont.setBold(true);
//            headerStyle.setFont(headerFont);
//            cell.setCellStyle(headerStyle);
//        }
//    }
//
//    private void addItemToSheet(Sheet sheet, int rowNum, Item item) {
//        Row row = sheet.createRow(rowNum);
//
//        row.createCell(0).setCellValue(item.getId());
//        row.createCell(1).setCellValue(item.getName());
//        row.createCell(2).setCellValue(item.getDescription());
//        row.createCell(3).setCellValue(item.getPrice());
//
//        CellStyle dateStyle = sheet.getWorkbook().createCellStyle();
//        dateStyle.setDataFormat(sheet.getWorkbook()
//                .getCreationHelper()
//                .createDataFormat()
//                .getFormat("dd.MM.yyyy HH:mm"));
//
//        Cell createdAtCell = row.createCell(4);
//        createdAtCell.setCellValue(item.getCreatedAt());
//        createdAtCell.setCellStyle(dateStyle);
//
//        Cell updatedAtCell = row.createCell(5);
//        updatedAtCell.setCellValue(item.getUpdatedAt());
//        updatedAtCell.setCellStyle(dateStyle);
//    }
//
//    // Метод для добавления новых данных с синхронизацией
//    @Transactional
//    public Mono<Void> addNewItemWithSync(Item item) {
//        return Mono.fromCallable(() -> {
//            // 1. Сохраняем в БД
//            Item savedItem = itemRepository.save(item);
//
//            // 2. Добавляем в последний Excel-файл (если нужно)
//            if (shouldAddToLatestExport()) {
//                addItemToLatestExport(savedItem);
//            }
//
//            return null;
//        });
//    }
//
//    private boolean shouldAddToLatestExport() {
//        // Логика определения нужно ли добавлять в существующий файл
//        return exportHistoryRepository.count() > 0;
//    }
//
//    private void addItemToLatestExport(Item item) {
//        exportHistoryRepository.findTopByOrderByExportDateDesc()
//                .ifPresent(latestExport -> {
//                    // Логика добавления в существующий файл
//                    // Можно реализовать через:
//                    // 1. Дозапись в существующий файл
//                    // 2. Создание нового файла с обновленными данными
//                    // 3. Систему версионности файлов
//                });
//    }
//}