package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    private static final long RECEIVER_ID = 99L;
    private static final EventType EVENT_TYPE = EventType.FOLLOWER;
    private static final long FIRST_EVENT_ID = 15L;
    private static final long SECOND_EVENT_ID = 24L;
    private static final long THIRD_EVENT_ID = 33L;
    private static final LocalDateTime FIRST_EVENT_TIME = LocalDateTime.of(2024, 11, 4, 12, 30);
    private static final LocalDateTime SECOND_EVENT_TIME = LocalDateTime.of(2023, 11, 4, 12, 30);
    private static final LocalDateTime THIRD_EVENT_TIME = LocalDateTime.of(2024, 11, 5, 12, 30);
    private static final LocalDateTime START_DATE = LocalDateTime.of(2024, 10, 4, 12, 30);
    private static final LocalDateTime END_DATE = LocalDateTime.of(2024, 12, 4, 12, 30);
    private static final Interval INTERVAL = Interval.YEAR;

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;

    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;

    private AnalyticsEvent firstEvent;
    private AnalyticsEvent secondEvent;
    private AnalyticsEvent thirdEvent;

    @BeforeEach
    public void setUp() {
        firstEvent = createAnalyticsEvent(FIRST_EVENT_ID, FIRST_EVENT_TIME);
        secondEvent = createAnalyticsEvent(SECOND_EVENT_ID, SECOND_EVENT_TIME);
        thirdEvent = createAnalyticsEvent(THIRD_EVENT_ID, THIRD_EVENT_TIME);
    }

    @Test
    void testSaveSuccess() {
        analyticsEventService.save(firstEvent);
        verify(analyticsEventRepository).save(firstEvent);
    }

    @Test
    void testGetAnalyticsSuccess() {
        List<AnalyticsEvent> analyticsEvents = List.of(firstEvent, secondEvent, thirdEvent);
        when(analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(
                RECEIVER_ID, EVENT_TYPE, START_DATE, END_DATE))
                .thenReturn(analyticsEvents.stream());

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                RECEIVER_ID, EVENT_TYPE, null, START_DATE, END_DATE
        );

        assertEquals(3, result.size());
        assertEquals(THIRD_EVENT_ID, result.get(0).getId());
        assertEquals(FIRST_EVENT_ID, result.get(1).getId());
        assertEquals(SECOND_EVENT_ID, result.get(2).getId());
    }

    @Test
    void testGetAnalyticsWithIntervalSuccess() {
        lenient().when(analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtAfter(
                        anyLong(), any(), any()))
                .thenReturn(Stream.of(firstEvent, secondEvent, thirdEvent));

        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(
                RECEIVER_ID, EVENT_TYPE, INTERVAL, null, null
        );

        assertEquals(3, result.size());
        assertEquals(THIRD_EVENT_ID, result.get(0).getId());
        assertEquals(FIRST_EVENT_ID, result.get(1).getId());
        assertEquals(SECOND_EVENT_ID, result.get(2).getId());

        verify(analyticsEventRepository, times(1))
                .findByReceiverIdAndEventTypeAndReceivedAtAfter(anyLong(), any(), any());
    }

    private AnalyticsEvent createAnalyticsEvent(long id, LocalDateTime receivedAt) {
        return AnalyticsEvent.builder()
                .eventType(EVENT_TYPE)
                .id(id)
                .receiverId(RECEIVER_ID)
                .receivedAt(receivedAt)
                .build();
    }
}
