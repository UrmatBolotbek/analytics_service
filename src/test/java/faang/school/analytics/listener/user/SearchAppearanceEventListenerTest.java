package faang.school.analytics.listener.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.analytics.event.SearchAppearanceEvent;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAppearanceEventListenerTest {

    @Captor
    private ArgumentCaptor<AnalyticsEvent> analyticsEventCaptor;

    @InjectMocks
    private SearchAppearanceEventListener listener;

    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Spy
    private AnalyticsEventMapperImpl mapper;

    private Message message;

    private SearchAppearanceEvent searchAppearanceEvent;

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

        searchAppearanceEvent = SearchAppearanceEvent.builder()
                .userIds(List.of(10L, 20L))
                .searchingUserId(99L)
                .viewedAt(LocalDateTime.of(2024, 12, 12, 12, 12))
                .build();
    }

    @Test
    void testOnMessageSuccess() throws IOException {
        byte[] pattern = new byte[]{1, 2, 3, 4};
        when(objectMapper.readValue(any(byte[].class), eq(SearchAppearanceEvent.class)))
                .thenReturn(searchAppearanceEvent);

        listener.onMessage(message, pattern);

        verify(analyticsEventService, times(2)).save(analyticsEventCaptor.capture());

        List<AnalyticsEvent> capturedEvents = analyticsEventCaptor.getAllValues();

        assertEquals(2, capturedEvents.size());
        AnalyticsEvent firstEvent = capturedEvents.get(0);
        assertEquals(10L, firstEvent.getReceiverId());
        assertEquals(99L, firstEvent.getActorId());
        assertEquals(searchAppearanceEvent.getViewedAt(), firstEvent.getReceivedAt());

        AnalyticsEvent secondEvent = capturedEvents.get(1);
        assertEquals(20L, secondEvent.getReceiverId());
        assertEquals(99L, secondEvent.getActorId());
        assertEquals(searchAppearanceEvent.getViewedAt(), secondEvent.getReceivedAt());
    }
}