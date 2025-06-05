package project.assay.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AgeRange {
    private int years;
    private int months;
    private int days;

    public int calculateTotalDays() {
        return years * 365 + months * 30 + days;
    }

    public static AgeRange convertToRange(int totalDays) {
        AgeRange ageRange = new AgeRange();
        ageRange.years = totalDays / 365;
        ageRange.months = totalDays % 365 / 30;
        ageRange.days = totalDays % 365 % 30;
        return ageRange;
    }
}
