package faang.school.analytics.message.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.dto.goal_completed_event.GoalCompletedEvent;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;
import org.jetbrains.annotations.NotNull;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GoalCompletedEventListenerTest {

    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    @InjectMocks
    private GoalCompletedEventListener listener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private AnalyticsEventMapperImpl mapper;

    private Message message;
    private GoalCompletedEvent goalEvent;

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
        goalEvent = GoalCompletedEvent.builder()
                .goalId(10L)
                .userId(99L)
                .build();
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        byte[] pattern = new byte[]{1,2,3,4};

        when(objectMapper.readValue(message.getBody(), GoalCompletedEvent.class)).thenReturn(goalEvent);
        listener.onMessage(message, pattern);
        verify(analyticsEventService).save(analyticsEventCaptor.capture());
        AnalyticsEvent analyticsEvent = analyticsEventCaptor.getValue();
        assertEquals(EventType.GOAL_COMPLETED, analyticsEvent.getEventType());
    }

}
