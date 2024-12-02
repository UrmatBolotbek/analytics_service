package faang.school.analytics.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Interval {

    private int number;
    private IntervalType intervalType;

}
