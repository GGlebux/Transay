package project.assay.utils;

import org.apache.poi.ss.usermodel.*;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.apache.poi.ss.usermodel.Row.MissingCellPolicy.CREATE_NULL_AS_BLANK;
import static org.apache.poi.ss.usermodel.WorkbookFactory.create;

public class StaticMethods {

    public static int getDaysOfAge(LocalDate startDate) {
        LocalDate today = now();
        return (int) DAYS.between(startDate, today);
    }

    public static int getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return (int) DAYS.between(startDate, endDate);
    }

    public static boolean isFuture(LocalDate date) {
        return date.isAfter(now());
    }

    public static Set<String> parseExcelColumn(String filePath, int columnIndex) {
        Set<String> result = new HashSet<>();

        try (InputStream inputStream = new ClassPathResource(filePath).getInputStream()) {
            Workbook workbook = create(inputStream);

            Sheet sheet = workbook.getSheetAt(0); // Берём первый лист
            for (Row row : sheet) {
                Cell cell = row.getCell(columnIndex, CREATE_NULL_AS_BLANK);
                String value = cell.toString().trim();
                if (!value.isEmpty()) {
                    result.add(value);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }
}