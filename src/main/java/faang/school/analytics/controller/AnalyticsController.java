package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.dto.LocalDateTimeInput;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.validator.AnalyticsControllerValidator;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/analytics")
public class AnalyticsController {

    private final AnalyticsEventService analyticsEventService;
    private final AnalyticsControllerValidator validator;

    @GetMapping
    public List<AnalyticsEventResponseDto> getAnalytics(@RequestParam @Positive long receiverId,
                                                        @RequestParam(required = false) String eventTypeString,
                                                        @RequestParam(required = false) @Positive Integer eventTypeInteger,
                                                        @RequestParam(required = false) String intervalString,
                                                        @RequestParam(required = false) @Positive Integer intervalInteger,
                                                        @RequestParam(required = false) LocalDateTimeInput startDate,
                                                        @RequestParam(required = false) LocalDateTimeInput endDate) {

        var validatedParams = validator.validateRequestParams(receiverId, eventTypeString, eventTypeInteger,
                intervalString, intervalInteger, startDate, endDate);

        return analyticsEventService.getAnalytics(
                receiverId,
                validatedParams.eventType(),
                validatedParams.interval(),
                validatedParams.startDate(),
                validatedParams.endDate()
        );
    }
}
