package faang.school.analytics.service.analytics_event;

import faang.school.analytics.dto.analytics_event.AnalyticsEventResponseDto;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    public void save(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
        log.info("Saving an object {} to the database ", analyticsEvent.getEventType());
    }

    public List<AnalyticsEventResponseDto> getAnalytics(long receiverId, EventType eventType, LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> analyticsEventList = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType);
        if (analyticsEventList == null) {
            log.warn("No analytics available for parameters {} {}", receiverId, eventType);
            return null;
        }
        log.info("Getting analytics events from {} to {}", from, to);
        return analyticsEventList.filter(analyticsEvent -> isInPeriod(analyticsEvent, from, to))
                .sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(mapper::toDto)
                .toList();
    }

    private boolean isInPeriod(AnalyticsEvent analyticsEvent, LocalDateTime from, LocalDateTime to) {
        LocalDateTime timeFromAnalyticsEvent = analyticsEvent.getReceivedAt();
        return timeFromAnalyticsEvent.isAfter(from) && timeFromAnalyticsEvent.isBefore(to);
    }

}
