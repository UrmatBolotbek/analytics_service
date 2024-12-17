package faang.school.analytics.listener.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.FundRaisedEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
public class FundRaisedEventListener extends AbstractEventListener<FundRaisedEvent> implements MessageListener {

    public FundRaisedEventListener(AnalyticsEventService analyticsEventService,
                                   AnalyticsEventMapper analyticsEventMapper,
                                   ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, FundRaisedEvent.class, fundRaisedEvent -> {
            AnalyticsEvent entity = analyticsEventMapper.toEntity(fundRaisedEvent);
            entity.setEventType(EventType.fromEventClass(fundRaisedEvent.getClass()));
            analyticsEventService.save(entity);
        });
    }
}
