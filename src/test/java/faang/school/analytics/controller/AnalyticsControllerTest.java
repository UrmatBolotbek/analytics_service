package faang.school.analytics.controller;

import faang.school.analytics.dto.AnalyticsEventResponseDto;
import faang.school.analytics.model.EventType;
import faang.school.analytics.service.AnalyticsEventService;
import faang.school.analytics.validator.AnalyticsControllerValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AnalyticsControllerTest {

    private static final long RECEIVER_ID = 99L;
    private static final long ACTOR_ID = 1L;
    private static final long EVENT_ID = 15L;
    private static final EventType EVENT_TYPE = EventType.FOLLOWER;
    private static final String EVENT_TYPE_STRING = "FOLLOWER";
    private static final String INTERVAL_STRING = "YEAR";

    private MockMvc mockMvc;

    @Mock
    private AnalyticsEventService analyticsEventService;

    @Mock
    private AnalyticsControllerValidator validator;

    @InjectMocks
    private AnalyticsController analyticsController;

    private AnalyticsEventResponseDto responseDto;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(analyticsController).build();

        responseDto = AnalyticsEventResponseDto.builder()
                .id(EVENT_ID)
                .receiverId(RECEIVER_ID)
                .actorId(ACTOR_ID)
                .eventType(EVENT_TYPE)
                .build();
    }

    @Test
    public void testGetAnalyticsSuccess() throws Exception {
        when(validator.validateRequestParams(
                RECEIVER_ID, EVENT_TYPE_STRING, null, INTERVAL_STRING, null, null, null))
                .thenReturn(new AnalyticsControllerValidator.ValidatedParams(EVENT_TYPE, null, null, null));

        when(analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, null, null))
                .thenReturn(List.of(responseDto));

        mockMvc.perform(get("/api/v1/analytics")
                        .param("receiverId", String.valueOf(RECEIVER_ID))
                        .param("eventTypeString", EVENT_TYPE_STRING)
                        .param("intervalString", INTERVAL_STRING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(EVENT_ID))
                .andExpect(jsonPath("$[0].receiverId").value(RECEIVER_ID))
                .andExpect(jsonPath("$[0].actorId").value(1L))
                .andExpect(jsonPath("$[0].eventType").value(EVENT_TYPE.name()));

        verify(validator).validateRequestParams(
                RECEIVER_ID, EVENT_TYPE_STRING, null, INTERVAL_STRING, null, null, null);
        verify(analyticsEventService).getAnalytics(RECEIVER_ID, EVENT_TYPE, null, null, null);
    }

    @Test
    public void testGetAnalyticsNoResults() throws Exception {
        when(validator.validateRequestParams(
                RECEIVER_ID, EVENT_TYPE_STRING, null, INTERVAL_STRING, null, null, null))
                .thenReturn(new AnalyticsControllerValidator.ValidatedParams(EVENT_TYPE, null, null, null));

        when(analyticsEventService.getAnalytics(RECEIVER_ID, EVENT_TYPE, null, null, null))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/v1/analytics")
                        .param("receiverId", String.valueOf(RECEIVER_ID))
                        .param("eventTypeString", EVENT_TYPE_STRING)
                        .param("intervalString", INTERVAL_STRING)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));

        verify(validator).validateRequestParams(
                RECEIVER_ID, EVENT_TYPE_STRING, null, INTERVAL_STRING, null, null, null);
        verify(analyticsEventService).getAnalytics(RECEIVER_ID, EVENT_TYPE, null, null, null);
    }
}
