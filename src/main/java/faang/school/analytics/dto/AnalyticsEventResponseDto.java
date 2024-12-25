package faang.school.analytics.dto;

import faang.school.analytics.model.EventType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsEventResponseDto {
    private long id;
    private long receiverId;
    private long actorId;
    private EventType eventType;
}
