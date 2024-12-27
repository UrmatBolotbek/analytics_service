package faang.school.analytics.listener.project.project_view;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.listener.project.ProjectViewEventListener;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProjectViewEventListenerTest {

    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    @InjectMocks
    private ProjectViewEventListener listener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private AnalyticsEventMapper mapper;

    private Message message;
    private ProjectViewEvent viewEvent;
    private AnalyticsEvent analyticsEvent;

    @BeforeEach
    public void setup() {
        message = new Message() {
            @Override
            public byte @NotNull [] getBody() {
                return new byte[15];
            }

            @Override
            public byte @NotNull [] getChannel() {
                return new byte[0];
            }
        };
        viewEvent = ProjectViewEvent.builder()
                .projectId(10L)
                .userId(99L)
                .eventTime(LocalDateTime.of(2024,12,12,12,12))
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .actorId(99L)
                .receiverId(10L)
                .receivedAt(LocalDateTime.of(2024,12,12,12,12))
                .build();
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        byte[] pattern = new byte[]{1,2,3,4};

        when(mapper.toAnalyticsEvent(viewEvent)).thenReturn(analyticsEvent);
        when(objectMapper.readValue(message.getBody(), ProjectViewEvent.class)).thenReturn(viewEvent);

        listener.onMessage(message, pattern);
        verify(analyticsEventService).save(analyticsEventCaptor.capture());
        AnalyticsEvent analyticsEvent = analyticsEventCaptor.getValue();
        assertEquals(EventType.PROJECT_VIEW, analyticsEvent.getEventType());
    }

}
