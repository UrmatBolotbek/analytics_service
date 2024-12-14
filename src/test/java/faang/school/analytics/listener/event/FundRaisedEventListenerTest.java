package faang.school.analytics.listener.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.FundRaisedEvent;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.connection.Message;

import java.io.IOException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FundRaisedEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private Message message;

    @InjectMocks
    private FundRaisedEventListener fundRaisedEventListener;

    private AnalyticsEvent analyticsEvent;
    private FundRaisedEvent fundRaisedEvent;

    @BeforeEach
    void setUp() {
        fundRaisedEvent = FundRaisedEvent.builder().build();

        analyticsEvent = AnalyticsEvent.builder()
                .eventType(EventType.fromEventClass(fundRaisedEvent.getClass()))
                .build();

        String json = """
                {
                    "userId": 1,
                    "projectId": 2,
                    "amount": 10000,
                    "donatedAt": "2024-12-15T13:35:15"
                }""";

        when(message.getBody()).thenReturn(json.getBytes());
    }

    @Test
    @DisplayName("Should handle event successfully")
    void onMessageShouldHandleEventSuccessfully() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(FundRaisedEvent.class)))
                .thenReturn(fundRaisedEvent);
        when(analyticsEventMapper.toEntity(fundRaisedEvent)).thenReturn(analyticsEvent);

        fundRaisedEventListener.onMessage(message, null);

        verify(analyticsEventMapper).toEntity(fundRaisedEvent);
        verify(analyticsEventService).save(analyticsEvent);
    }

    @Test
    void onMessageShouldThrowRuntimeExceptionWhenDeserializationFails() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(FundRaisedEvent.class)))
                .thenThrow(new JsonProcessingException("Test exception") {
                });

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                fundRaisedEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).save(any());
    }
}
