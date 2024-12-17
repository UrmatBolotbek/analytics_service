package faang.school.analytics.listener.premium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.FundRaisedEvent;
import faang.school.analytics.event.PremiumBoughtEvent;
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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PremiumBoughtEventListenerTest {
    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventMapper analyticsEventMapper;

    @Mock
    private Message message;

    @InjectMocks
    private PremiumBoughtEventListener premiumBoughtEventListener;

    private AnalyticsEvent analyticsEvent;
    private PremiumBoughtEvent premiumBoughtEvent;

    @BeforeEach
    void setUp() {
        premiumBoughtEvent = PremiumBoughtEvent.builder().build();

        analyticsEvent = AnalyticsEvent.builder()
                .eventType(EventType.fromEventClass(premiumBoughtEvent.getClass()))
                .build();

        String json = """
                {
                    "userId": 1,
                    "amount": 10,
                    "period": 30,
                    "purchaseDate": "2024-12-15T13:35:15"
                }""";

        when(message.getBody()).thenReturn(json.getBytes());
    }

    @Test
    @DisplayName("Should handle event successfully")
    void onMessageShouldHandleEventSuccessfully() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(PremiumBoughtEvent.class)))
                .thenReturn(premiumBoughtEvent);
        when(analyticsEventMapper.toEntity(premiumBoughtEvent)).thenReturn(analyticsEvent);

        premiumBoughtEventListener.onMessage(message, null);

        verify(analyticsEventMapper).toEntity(premiumBoughtEvent);
        verify(analyticsEventService).save(analyticsEvent);
    }

    @Test
    void onMessageShouldThrowRuntimeExceptionWhenDeserializationFails() throws IOException {
        when(objectMapper.readValue(any(byte[].class), eq(FundRaisedEvent.class)))
                .thenThrow(new JsonProcessingException("Test exception") {
                });

        assertThatExceptionOfType(RuntimeException.class).isThrownBy(() ->
                premiumBoughtEventListener.onMessage(message, null));
        verify(analyticsEventService, never()).save(any());
    }
}
