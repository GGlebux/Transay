package project.assay.utils;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.apache.poi.ss.usermodel.WorkbookFactory.create;

public class StaticMethods {

    public static int getDaysOfAge(LocalDate startDate) {
        LocalDate today = now();
        return (int) DAYS.between(startDate, today);
    }

    public static int getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return (int) DAYS.between(startDate, endDate);
    }

    public static String genderToWord(String gender) {
        return gender.equals("male") ? "Мужской" : "Женский";
    }

    public static String boolToWord(boolean bool) {
        return bool ? "Да" : "Нет";
    }

    public static boolean isFuture(LocalDate date) {
        return date.isAfter(now());
    }
}