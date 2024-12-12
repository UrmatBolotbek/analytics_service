package faang.school.analytics.listener.bought;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.AdBoughtEvent;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.analytics_event.AnalyticsEventService;


import jakarta.validation.constraints.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class AdBoughtEventListenerTest {
    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventArgumentCaptor;

    @InjectMocks
    private AdBoughtEventListener listener;
    @Mock
    private ObjectMapper objectMapper;
    @Mock
    private AnalyticsEventService analyticsEventService;
    @Spy
    private AnalyticsEventMapperImpl mapper;

    private Message message;
    private AdBoughtEvent adBoughtEvent;
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
        adBoughtEvent = AdBoughtEvent.builder()
                .postId(10L)
                .userId(99L)
                .paymentAmount(20.5)
                .duration(3)
                .purchaseTime(LocalDateTime.of(2024, 12, 12, 12, 22))
                .build();
        analyticsEvent = AnalyticsEvent.builder()
                .actorId(99L)
                .receiverId(10L)
                .receivedAt(LocalDateTime.of(2024,12,12,12,12))
                .build();
    }

    @Test
    void testOnMessageSuccess() throws Exception {
        byte[] pattern = new byte[]{1, 2, 3, 4};

        when(mapper.toAnalyticsEvent(adBoughtEvent)).thenReturn(analyticsEvent);
        when(objectMapper.readValue(message.getBody(), AdBoughtEvent.class)).thenReturn(adBoughtEvent);
        listener.onMessage(message, pattern);
        verify(analyticsEventService).save(analyticsEventArgumentCaptor.capture());
        AnalyticsEvent capturedEvent = analyticsEventArgumentCaptor.getValue();
        assertEquals(EventType.PROJECT_VIEW, capturedEvent.getEventType());
    }
}
