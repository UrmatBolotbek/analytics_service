package faang.school.analytics.service.analytics_event;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;

    public void save(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
        log.info("Saving an object {} to the database ", analyticsEvent.getEventType());
    }

    public List<AnalyticsEventResponseDto> getAnalytics(long receiverId, EventType eventType, Interval interval,
                                                        LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> analyticsEventList = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType);



    }






}
