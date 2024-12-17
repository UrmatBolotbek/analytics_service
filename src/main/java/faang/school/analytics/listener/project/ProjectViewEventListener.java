package faang.school.analytics.listener.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProjectViewEvent;
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

@Component
@Slf4j
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEvent> implements MessageListener {

    public ProjectViewEventListener(ObjectMapper objectMapper,
                                    AnalyticsEventService analyticsEventService,
                                    AnalyticsEventMapper analyticsEventMapper) {
        super(analyticsEventService, analyticsEventMapper, objectMapper);
    }

    @Override
    public void onMessage(@NonNull Message message, byte[] pattern) {
        handleEvent(message, ProjectViewEvent.class, event -> {
            AnalyticsEvent analyticsEvent = analyticsEventMapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.fromEventClass(event.getClass()));
            analyticsEventService.save(analyticsEvent);
            log.info("The project {} was viewed by the user {}", analyticsEvent.getId(), event.getUserId());
        });
    }
}

