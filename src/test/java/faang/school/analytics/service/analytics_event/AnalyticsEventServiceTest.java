package faang.school.analytics.service.analytics_event;

import faang.school.analytics.dto.analytics_event.AnalyticsEventResponseDto;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapperImpl;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AnalyticsEventServiceTest {

    @InjectMocks
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsEventRepository analyticsEventRepository;
    @Spy
    private AnalyticsEventMapperImpl analyticsEventMapper;
    private AnalyticsEvent firstEvent;
    private AnalyticsEvent secondEvent;
    private AnalyticsEvent thirdEvent;
    private AnalyticsEventResponseDto responseDto;

    @BeforeEach
    public void setUp() {
        firstEvent = AnalyticsEvent.builder()
                .eventType(EventType.FOLLOWER)
                .id(15L)
                .receiverId(99L)
                .receivedAt(LocalDateTime.of(2024,11,4,12,30))
                .build();
        secondEvent = AnalyticsEvent.builder()
                .eventType(EventType.POST_LIKE)
                .id(24L)
                .receiverId(99L)
                .receivedAt(LocalDateTime.of(2023,11,4,12,30))
                .build();
        thirdEvent = AnalyticsEvent.builder()
                .eventType(EventType.FOLLOWER)
                .id(33L)
                .receiverId(99L)
                .receivedAt(LocalDateTime.of(2024,11,5,12,30))
                .build();
        responseDto = AnalyticsEventResponseDto.builder()
                .eventType(EventType.FOLLOWER)
                .id(15L)
                .receiverId(99L)
                .build();
    }

    @Test
    void testSaveSuccess() {
        analyticsEventService.save(firstEvent);
        verify(analyticsEventRepository).save(firstEvent);
    }

    @Test
    void testGetAnalyticsSuccess() {
        List<AnalyticsEvent> analyticsEvents = List.of(firstEvent, secondEvent, thirdEvent);
        when(analyticsEventRepository.findByReceiverIdAndEventType(99L, EventType.FOLLOWER))
                .thenReturn(analyticsEvents.stream());
        List<AnalyticsEventResponseDto> result = analyticsEventService.getAnalytics(99L, EventType.FOLLOWER,
                LocalDateTime.of(2024,10,4,12,30),
                LocalDateTime.of(2024,12,4,12,30));
        assertEquals(result.get(0), responseDto);
        assertEquals(result.size(), 2);
    }

}
