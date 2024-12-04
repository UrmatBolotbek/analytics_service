package faang.school.analytics.listener.project;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.listener.AbstractEventListener;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProjectViewEventListener extends AbstractEventListener<ProjectViewEvent> implements MessageListener {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsEventMapper mapper;

    public ProjectViewEventListener(ObjectMapper objectMapper,
                                      AnalyticsEventService analyticsEventService,
                                      AnalyticsEventMapper mapper) {
        super(objectMapper);
        this.analyticsEventService = analyticsEventService;
        this.mapper = mapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        handleEvent(message, ProjectViewEvent.class, event -> {
            AnalyticsEvent analyticsEvent = mapper.toAnalyticsEvent(event);
            analyticsEvent.setEventType(EventType.fromEventClass(event.getClass()));
            analyticsEventService.save(analyticsEvent);
            log.info("The project {} was viewed by the user {}", analyticsEvent.getId(), event.getUserId());
        });
    }
}

