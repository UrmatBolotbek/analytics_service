package faang.school.analytics.listener.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class GoalCompletedEventListener extends AbstractEventListener<GoalCompletedEvent> implements MessageListener {


    public GoalCompletedEventListener(ObjectMapper objectMapper,
                                      AnalyticsEventService analyticsEventService,
                                      AnalyticsEventMapper analyticsEventMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, GoalCompletedEvent.class, event -> {
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.fromEventClass(event.getClass()));
            analyticsEventService.save(analyticsEvent);
            log.info("Goal completed: {}", analyticsEvent.getReceiverId());
        });
    }
}
