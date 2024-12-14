package faang.school.analytics.validator;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.exception.DataValidationException;
import faang.school.analytics.model.EventType;
import faang.school.analytics.model.Interval;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AnalyticsControllerValidator {

    public ValidatedParams validateRequestParams(Long receiverId,
                                                 String eventTypeString, Integer eventTypeInteger,
                                                 String intervalString, Integer intervalInteger,
                                                 LocalDateTimeInput startInput, LocalDateTimeInput endInput) {
        log.debug("Starting request params validation: receiverId={}, eventTypeString={}, eventTypeInteger={}," +
                        " intervalString={}, intervalInteger={}, startInput={}, endInput={}",
                receiverId, eventTypeString, eventTypeInteger, intervalString, intervalInteger, startInput, endInput);

        validateReceiverId(receiverId);

        EventType eventType = parseEventType(eventTypeString, eventTypeInteger);
        Interval interval = parseInterval(intervalString, intervalInteger);

        LocalDateTime startDate = startInput == null ? null : startInput.getDateTime();
        LocalDateTime endDate = endInput == null ? null : endInput.getDateTime();

        validateIntervalOrDates(interval, startDate, endDate);
        validateDateRange(startDate, endDate);

        log.debug("Request params validated successfully. Resolved eventType={}, interval={}, startDate={}, endDate={}",
                eventType, interval, startDate, endDate);

        return new ValidatedParams(eventType, interval, startDate, endDate);
    }

    private void validateReceiverId(Long receiverId) {
        log.debug("Validating receiverId: {}", receiverId);
        if (receiverId == null || receiverId <= 0) {
            log.warn("Invalid receiverId: {}", receiverId);
            throw new DataValidationException("Invalid receiverId: must be a positive number");
        }
        log.debug("receiverId is valid");
    }

    private EventType parseEventType(String eventTypeString, Integer eventTypeInteger) {
        log.debug("Parsing eventType from eventTypeString={} or eventTypeInteger={}", eventTypeString, eventTypeInteger);

        if (eventTypeString == null && eventTypeInteger == null) {
            log.warn("Both eventTypeString and eventTypeInteger are null");
            throw new DataValidationException("EventType is required (provide either a string or an integer)");
        }

        if (eventTypeString != null) {
            return parseEventTypeFromString(eventTypeString);
        }

        return parseEventTypeFromInteger(eventTypeInteger);
    }

    private EventType parseEventTypeFromString(String eventTypeString) {
        try {
            return EventType.fromString(eventTypeString);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid eventType string: {}", eventTypeString);
            throw new DataValidationException("Invalid event type: " + eventTypeString);
        }
    }

    private EventType parseEventTypeFromInteger(Integer eventTypeInteger) {
        try {
            return EventType.of(eventTypeInteger);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid eventType index: {}", eventTypeInteger);
            throw new DataValidationException("Invalid event type index: " + eventTypeInteger);
        }
    }

    private Interval parseInterval(String intervalString, Integer intervalInteger) {
        log.debug("Parsing interval from intervalString={} or intervalInteger={}", intervalString, intervalInteger);

        if (intervalString == null && intervalInteger == null) {
            log.debug("No interval provided");
            return null;
        }

        if (intervalString != null) {
            return parseIntervalFromString(intervalString);
        }

        return parseIntervalFromInteger(intervalInteger);
    }

    private Interval parseIntervalFromString(String intervalString) {
        try {
            return Interval.fromString(intervalString);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid interval string: {}", intervalString);
            throw new DataValidationException("Invalid interval: " + intervalString);
        }
    }

    private Interval parseIntervalFromInteger(Integer intervalInteger) {
        try {
            return Interval.of(intervalInteger);
        } catch (IllegalArgumentException e) {
            log.warn("Invalid interval index: {}", intervalInteger);
            throw new DataValidationException("Invalid interval index: " + intervalInteger);
        }
    }

    private void validateIntervalOrDates(Interval interval, LocalDateTime startDate, LocalDateTime endDate) {
        boolean intervalProvided = interval != null;
        boolean datesProvided = (startDate != null && endDate != null);

        if (!intervalProvided && !datesProvided) {
            log.warn("Neither interval nor date range provided");
            throw new DataValidationException("Either interval must be provided or both startDate and endDate must be specified");
        }

        if (intervalProvided && datesProvided) {
            log.warn("Both interval and date range provided, which is not allowed");
            throw new DataValidationException("Provide either interval OR startDate/endDate, not both");
        }

        log.debug("Interval/dates condition is satisfied");
    }

    private void validateDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        if ((startDate != null && endDate == null) || (startDate == null && endDate != null)) {
            log.warn("Only one of startDate or endDate is provided: startDate={}, endDate={}", startDate, endDate);
            throw new DataValidationException("Both startDate and endDate must be provided together");
        }

        if (startDate != null && startDate.isAfter(endDate)) {
            log.warn("Start date is after end date: startDate={}, endDate={}", startDate, endDate);
            throw new DataValidationException("startDate must not be after endDate");
        }

        if (startDate != null) {
            log.debug("Valid date range provided: {} to {}", startDate, endDate);
        }
    }

    public record ValidatedParams(EventType eventType, Interval interval, LocalDateTime startDate,
                                  LocalDateTime endDate) {
    }
}
