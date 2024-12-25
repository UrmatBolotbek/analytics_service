package faang.school.analytics.validator;

import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.exception.DataValidationException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AnalyticsControllerValidatorTest {

    private final AnalyticsControllerValidator validator = new AnalyticsControllerValidator();

    @Test
    void validateValidRequestParams() {
        LocalDateTimeInput startInput = new LocalDateTimeInput();
        startInput.setDateTime(LocalDateTime.of(2024, 12, 1, 0, 0));

        LocalDateTimeInput endInput = new LocalDateTimeInput();
        endInput.setDateTime(LocalDateTime.of(2024, 12, 31, 23, 59));

        assertDoesNotThrow(() -> validator.validateRequestParams(
                99L,
                "FOLLOWER", null,
                null, null,
                startInput, endInput
        ));
    }

    @Test
    void validateValidRequestParamsWithInterval() {
        assertDoesNotThrow(() -> validator.validateRequestParams(
                99L,
                "FOLLOWER", null,
                "MONTH", null,
                null, null
        ));
    }

    @Test
    void validateRequestParamsMissingReceiverId() {
        LocalDateTimeInput startInput = new LocalDateTimeInput();
        startInput.setDateTime(LocalDateTime.of(2024, 12, 1, 0, 0));

        LocalDateTimeInput endInput = new LocalDateTimeInput();
        endInput.setDateTime(LocalDateTime.of(2024, 12, 31, 23, 59));

        assertThrows(DataValidationException.class, () -> validator.validateRequestParams(
                null,
                "FOLLOWER", null,
                "MONTH", null,
                startInput, endInput
        ));
    }

    @Test
    void validateRequestParamsInvalidEventType() {
        LocalDateTimeInput startInput = new LocalDateTimeInput();
        startInput.setDateTime(LocalDateTime.of(2024, 12, 1, 0, 0));

        LocalDateTimeInput endInput = new LocalDateTimeInput();
        endInput.setDateTime(LocalDateTime.of(2024, 12, 31, 23, 59));

        assertThrows(DataValidationException.class, () -> validator.validateRequestParams(
                99L,
                "INVALID_EVENT_TYPE", null,
                "MONTH", null,
                startInput, endInput
        ));
    }

    @Test
    void validateRequestParamsMissingEventType() {
        LocalDateTimeInput startInput = new LocalDateTimeInput();
        startInput.setDateTime(LocalDateTime.of(2024, 12, 1, 0, 0));

        LocalDateTimeInput endInput = new LocalDateTimeInput();
        endInput.setDateTime(LocalDateTime.of(2024, 12, 31, 23, 59));

        assertThrows(DataValidationException.class, () -> validator.validateRequestParams(
                99L,
                null, null,
                "MONTH", null,
                startInput, endInput
        ));
    }

    @Test
    void validateRequestParamsInvalidDateRange() {
        LocalDateTimeInput startInput = new LocalDateTimeInput();
        startInput.setDateTime(LocalDateTime.of(2024, 12, 31, 23, 59));

        LocalDateTimeInput endInput = new LocalDateTimeInput();
        endInput.setDateTime(LocalDateTime.of(2024, 12, 1, 0, 0));

        assertThrows(DataValidationException.class, () -> validator.validateRequestParams(
                99L,
                "FOLLOWER", null,
                null, null,
                startInput, endInput
        ));
    }

    @Test
    void validateRequestParamsMissingIntervalAndDates() {
        assertThrows(DataValidationException.class, () -> validator.validateRequestParams(
                99L,
                "FOLLOWER", null,
                null, null,
                null, null
        ));
    }

    @Test
    void validateRequestParamsIntervalAndDatesProvided() {
        LocalDateTimeInput startInput = new LocalDateTimeInput();
        startInput.setDateTime(LocalDateTime.of(2024, 12, 1, 0, 0));

        LocalDateTimeInput endInput = new LocalDateTimeInput();
        endInput.setDateTime(LocalDateTime.of(2024, 12, 31, 23, 59));

        assertThrows(DataValidationException.class, () -> validator.validateRequestParams(
                99L,
                "FOLLOWER", null,
                "MONTH", null,
                startInput, endInput
        ));
    }
}
