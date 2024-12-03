package faang.school.analytics.message.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.goal_completed_event.GoalCompletedEvent;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class GoalCompletedEventListener implements MessageListener {

    private final ObjectMapper objectMapper;
    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
          GoalCompletedEvent goalCompletedEvent = objectMapper.readValue(message.getBody(), GoalCompletedEvent.class);
          AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(goalCompletedEvent);
          analyticsEvent.setEventType(EventType.GOAL_COMPLETED);
          analyticsEventService.save(analyticsEvent);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
