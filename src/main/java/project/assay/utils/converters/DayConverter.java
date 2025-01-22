package project.assay.utils.converters;

import java.util.HashMap;
import java.util.Map;

public class DayConverter {
  static public Map<String, Integer> convertToPeriod(int allDays) {
    Map<String, Integer> period = new HashMap<>();

    int years = allDays / 365;
    int months = (allDays % 365) / 30;
    int weeks = (allDays % 365) % 30 / 7;
    int days = (allDays % 365) % 30 % 7;

    if (years > 0) {
      period.put("years", years);
    }
    if (months > 0) {
      period.put("months", months);
    }
    if (weeks > 0) {
      period.put("weeks", weeks);
    }
    if (days > 0) {
      period.put("days", days);
    }
    return period;
  }

  static public int convertToDays(Map<String, Integer> period) {
    int allDays = 0;
    allDays += period.get("years") * 365;
    allDays += period.get("months") * 30;
    allDays += period.get("weeks") * 7;
    allDays += period.get("days");
    return allDays;
  }
}
