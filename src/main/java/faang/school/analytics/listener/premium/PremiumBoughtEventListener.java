package faang.school.analytics.listener.premium;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.PremiumBoughtEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;

public class PremiumBoughtEventListener extends AbstractEventListener<PremiumBoughtEvent> implements MessageListener {
    public PremiumBoughtEventListener(AnalyticsEventService analyticsEventService,
                                      AnalyticsEventMapper analyticsEventMapper,
                                      ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, PremiumBoughtEvent.class, premiumBoughtEvent -> {
            AnalyticsEvent entity = analyticsEventMapper.toEntity(premiumBoughtEvent);
            entity.setEventType(EventType.fromEventClass(premiumBoughtEvent.getClass()));
            analyticsEventService.save(entity);
        });
    }
}
