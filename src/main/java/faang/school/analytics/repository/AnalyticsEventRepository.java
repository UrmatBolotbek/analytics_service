package faang.school.analytics.repository;

import faang.school.analytics.model.AnalyticsEvent;
import faang.school.analytics.model.EventType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Repository
public interface AnalyticsEventRepository extends CrudRepository<AnalyticsEvent, Long> {
    Stream<AnalyticsEvent> findByReceiverIdAndEventTypeAndReceivedAtAfter(long receiverId, EventType eventType, LocalDateTime from);
    Stream<AnalyticsEvent> findByReceiverIdAndEventTypeAndReceivedAtBetween(long receiverId, EventType eventType, LocalDateTime from, LocalDateTime to);
}
