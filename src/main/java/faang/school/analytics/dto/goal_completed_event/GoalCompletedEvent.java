package faang.school.analytics.dto.goal_completed_event;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class GoalCompletedEvent {
    private long goalId;
    private long userId;
    private LocalDateTime completedAt;
}
