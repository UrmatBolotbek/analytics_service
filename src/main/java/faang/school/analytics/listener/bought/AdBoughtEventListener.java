package faang.school.analytics.listener.bought;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.AdBoughtEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class AdBoughtEventListener extends AbstractEventListener<AdBoughtEvent> implements MessageListener {

    public AdBoughtEventListener(AnalyticsEventService analyticsEventService,
                                 AnalyticsEventMapper analyticsEventMapper,
                                 ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, AdBoughtEvent.class, event -> {
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.fromEventClass(event.getClass()));
            analyticsEventService.save(analyticsEvent);
        });
    }
}
