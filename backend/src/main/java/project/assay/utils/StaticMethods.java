package project.assay.utils;

import java.net.URI;
import java.time.LocalDate;

import static java.time.LocalDate.now;
import static java.time.temporal.ChronoUnit.DAYS;
import static org.springframework.web.servlet.support.ServletUriComponentsBuilder.fromCurrentRequest;

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


    public static URI getLocation(int id){
        return fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}