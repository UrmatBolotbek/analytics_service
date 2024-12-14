package faang.school.analytics.model;

import java.util.Locale;

public enum Interval {
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public static Interval of(int type) {
        for (Interval interval : Interval.values()) {
            if (interval.ordinal() == type) {
                return interval;
            }
        }
        throw new IllegalArgumentException("Unknown interval: " + type);
    }

    public static Interval fromString(String intervalString) {
        try {
            return valueOf(intervalString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid interval: " + intervalString);
        }
    }
}
