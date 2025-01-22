package project.assay.utils.converters;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class StaticMethods {
  public static int getDaysOfAge(LocalDate birthDate) {
    LocalDate today = LocalDate.now();
    return (int) ChronoUnit.DAYS.between(birthDate, today);
  }
}
