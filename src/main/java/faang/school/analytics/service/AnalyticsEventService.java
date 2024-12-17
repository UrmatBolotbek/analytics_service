package faang.school.analytics.service;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsEventService {

    private final AnalyticsEventRepository analyticsEventRepository;
    private final AnalyticsEventMapper mapper;

    public void save(AnalyticsEvent analyticsEvent) {
        analyticsEventRepository.save(analyticsEvent);
        log.info("Saved AnalyticsEvent of type {} for receiverId {}",
                analyticsEvent.getEventType(), analyticsEvent.getReceiverId());
    }

    public List<AnalyticsEventResponseDto> getAnalytics(long receiverId, EventType eventType,
                                                        Interval interval, LocalDateTime from, LocalDateTime to) {
        log.info("Fetching analytics for receiverId={}, eventType={}, interval={}, from={}, to={}",
                receiverId, eventType, interval, from, to);

        Stream<AnalyticsEvent> analyticsEventStream = fetchAnalyticsEvents(receiverId, eventType, interval, from, to);

        List<AnalyticsEventResponseDto> responseDtos = sortAndMapEvents(analyticsEventStream);

        logResults(receiverId, eventType, responseDtos);

        return responseDtos;
    }

    private Stream<AnalyticsEvent> fetchAnalyticsEvents(long receiverId, EventType eventType,
                                                        Interval interval, LocalDateTime from, LocalDateTime to) {
        if (interval != null) {
            LocalDateTime startDate = calculateStartDate(interval);
            log.info("Filtering events received on or after {}", startDate);
            return analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtAfter(receiverId, eventType, startDate);
        } else {
            log.info("Filtering events received between {} and {}", from, to);
            return analyticsEventRepository.findByReceiverIdAndEventTypeAndReceivedAtBetween(receiverId, eventType, from, to);
        }
    }

    private List<AnalyticsEventResponseDto> sortAndMapEvents(Stream<AnalyticsEvent> stream) {
        return stream.sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt).reversed())
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    private void logResults(long receiverId, EventType eventType, List<AnalyticsEventResponseDto> responseDtos) {
        if (responseDtos.isEmpty()) {
            log.warn("No analytics events found for receiverId={} and eventType={}", receiverId, eventType);
        } else {
            log.info("Found {} analytics events for receiverId={} and eventType={}", responseDtos.size(), receiverId, eventType);
        }
    }

    private LocalDateTime calculateStartDate(Interval interval) {
        LocalDateTime now = LocalDateTime.now();
        return switch (interval) {
            case DAY -> now.minusDays(1);
            case WEEK -> now.minusWeeks(1);
            case MONTH -> now.minusMonths(1);
            case YEAR -> now.minusYears(1);
        };
    }
}
