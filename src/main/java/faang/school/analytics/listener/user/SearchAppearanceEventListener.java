package faang.school.analytics.listener.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
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
public class SearchAppearanceEventListener extends AbstractEventListener<SearchAppearanceEvent> implements MessageListener {

    public SearchAppearanceEventListener(ObjectMapper objectMapper,
                                         AnalyticsEventService analyticsEventService,
                                         AnalyticsEventMapper analyticsEventMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, SearchAppearanceEvent.class, event -> event.getUserIds().forEach(userId -> {
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEventFromSearchAppearance(
                    userId, event.getSearchingUserId(), event.getViewedAt());

            analyticsEvent.setEventType(EventType.fromEventClass(event.getClass()));
            analyticsEventService.save(analyticsEvent);
            log.info("The user profile {} was viewed by the user {}", analyticsEvent.getId(), event.getSearchingUserId());
        }));
    }
}