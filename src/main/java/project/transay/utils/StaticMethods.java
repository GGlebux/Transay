package project.transay.utils;

import project.transay.models.ConditionEnum;
import project.transay.models.GenderEnum;

import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;

public class StaticMethods {

    public static int getDaysOfAge(LocalDate startDate) {
        LocalDate today = now();
        return (int) DAYS.between(startDate, today);
    }

    public static int getDaysBetween(LocalDate startDate, LocalDate endDate) {
        return (int) DAYS.between(startDate, endDate);
    }

    public static String genderToWord(GenderEnum gender) {
        return switch (gender) {
            case MALE -> "Мужской";
            case FEMALE -> "Женский";
            default -> "Оба";
        };
    }

    public static String conditionToWord(ConditionEnum condition) {
        return switch (condition) {
            case BASE -> "Базовый";
            case GRAVID -> "Беременность";
            case MENSES -> "Менструация";
        };
    }

    public static String boolToWord(boolean bool) {
        return bool ? "Да" : "Нет";
    }

    public static boolean isFuture(LocalDate date) {
        return date.isAfter(now());
    }
}