package faang.school.analytics.listener.subscription;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.FollowerEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FollowerEventListener extends AbstractEventListener<FollowerEvent> implements MessageListener {

    public FollowerEventListener(AnalyticsEventService analyticsEventService,
                                 AnalyticsEventMapper analyticsEventMapper,
                                 ObjectMapper objectMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, FollowerEvent.class, followerEvent -> {
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(followerEvent);
            analyticsEvent.setEventType(EventType.FOLLOWER);
            analyticsEventService.save(analyticsEvent);
            log.info("User {} has subscribed to the user {}", followerEvent.getFollowerId(), followerEvent.getFolloweeId());
        });
    }
}
