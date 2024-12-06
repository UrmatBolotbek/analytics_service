package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
public class ProjectViewEvent {

    private long projectId;
    private long userId;
    private LocalDateTime eventTime;

}
