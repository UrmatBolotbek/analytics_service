package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@Builder
public class SearchAppearanceEvent {
    private List<Long> userIds;
    private Long searchingUserId;
    private LocalDateTime viewedAt;
}