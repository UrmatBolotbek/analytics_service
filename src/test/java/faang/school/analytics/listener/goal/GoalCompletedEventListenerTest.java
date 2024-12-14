package faang.school.analytics.listener.goal;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
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
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        goalEvent = GoalCompletedEvent.builder()
                .goalId(10L)
                .userId(99L)
                .completedAt(LocalDateTime.of(2024,12,12,12,12))
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

        when(mapper.toAnalyticsEvent(goalEvent)).thenReturn(analyticsEvent);
        when(objectMapper.readValue(message.getBody(), GoalCompletedEvent.class)).thenReturn(goalEvent);

        listener.onMessage(message, pattern);
        verify(analyticsEventService).save(analyticsEventCaptor.capture());
        AnalyticsEvent analyticsEvent = analyticsEventCaptor.getValue();
        assertEquals(EventType.GOAL_COMPLETED, analyticsEvent.getEventType());
    }

}
