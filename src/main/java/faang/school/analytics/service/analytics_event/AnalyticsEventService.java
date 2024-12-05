package faang.school.analytics.service.analytics_event;

import faang.school.analytics.event.AnalyticsEventResponseDto;
import faang.school.analytics.mapper.analytics_event.AnalyticsEventMapper;
import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import faang.school.analytics.model.IntervalType;
import faang.school.analytics.repository.AnalyticsEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    public List<AnalyticsEventResponseDto> getAnalytics(long receiverId, EventType eventType,
                                                        Interval interval, LocalDateTime from, LocalDateTime to) {
        Stream<AnalyticsEvent> analyticsEventList = analyticsEventRepository
                .findByReceiverIdAndEventType(receiverId, eventType);
        if (analyticsEventList == null) {
            log.warn("No analytics available for parameters {} {}", receiverId, eventType);
            return new ArrayList<>();
        }
        if (interval != null) {
            LocalDateTime dateTime = getLocalDateTime(interval);
            Stream<AnalyticsEvent> analyticsEventStream = analyticsEventList
                    .filter(analyticsEvent -> analyticsEvent.getReceivedAt().isAfter(dateTime));
            log.info("Getting analytics events from {}", dateTime);
            return getSortedDtoList(analyticsEventStream);
        }
        Stream<AnalyticsEvent> analyticsEventStream = analyticsEventList
                .filter(analyticsEvent -> isInPeriod(analyticsEvent, from, to));
        log.info("Getting analytics events from {} to {}", from, to);
        return getSortedDtoList(analyticsEventStream);
    }

    private boolean isInPeriod(AnalyticsEvent analyticsEvent, LocalDateTime from, LocalDateTime to) {
        LocalDateTime timeFromAnalyticsEvent = analyticsEvent.getReceivedAt();
        return timeFromAnalyticsEvent.isAfter(from) && timeFromAnalyticsEvent.isBefore(to);
    }

    private LocalDateTime getLocalDateTime(Interval interval) {
        LocalDateTime now = LocalDateTime.now();
        int number = interval.getNumber();
        LocalDateTime newDate;
        IntervalType type = interval.getIntervalType();
        newDate = switch (type) {
            case DAY -> now.minusDays(number);
            case MONTH -> now.minusMonths(number);
            case YEAR -> now.minusYears(number);
        };
        return newDate;
    }

    private List<AnalyticsEventResponseDto> getSortedDtoList(Stream<AnalyticsEvent> analyticsEvent) {
        return  analyticsEvent.sorted(Comparator.comparing(AnalyticsEvent::getReceivedAt))
                .map(mapper::toDto)
                .toList();
    }

}
