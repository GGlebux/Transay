package project.transay.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import static java.lang.String.format;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgeRange {
    private int years;
    private int months;
    private int days;

    public int calculateTotalDays(boolean isExcluded) {
        int resultDays = years * 365 + months * 30 + days;
        return isExcluded ? resultDays - 1 : resultDays;
    }

    public static AgeRange daysToRange(int totalDays, boolean isExcluded) {
        AgeRange ageRange = new AgeRange();
        if (isExcluded) totalDays++;
        ageRange.years = totalDays / 365;
        ageRange.months = totalDays % 365 / 30;
        ageRange.days = totalDays % 365 % 30;
        return ageRange;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb
                .append("Период: ")
                .append(stringOf(days, "дни"))
                .append(stringOf(months, "месяцы"))
                .append(stringOf(years, "годы"));

        return sb.toString();
    }

    private String stringOf(int value, String name) {
        return value > 0 ? format("\n%s='%s'", name, value) : "";
    }
}
