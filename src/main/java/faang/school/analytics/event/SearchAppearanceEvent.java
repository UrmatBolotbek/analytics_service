package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class SearchAppearanceEvent {
    private Long userId;
    private Long searchingUserId;
    private LocalDateTime viewedAt;
}