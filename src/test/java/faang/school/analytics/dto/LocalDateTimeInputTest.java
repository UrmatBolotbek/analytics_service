package faang.school.analytics.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocalDateTimeInputTest {

    private final ObjectMapper objectMapper;

    public LocalDateTimeInputTest() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void testDeserializeValidDateTime() throws Exception {
        String json = "{\"dateTime\":\"14-12-2024 15:30:00\"}";
        objectMapper.setDateFormat(new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"));
        LocalDateTimeInput input = objectMapper.readValue(json, LocalDateTimeInput.class);

        assertEquals(LocalDateTime.of(2024, 12, 14, 15, 30), input.getDateTime());
    }

    @Test
    void testDeserializeInvalidDateTime() {
        String json = "{\"dateTime\":\"invalid-date\"}";

        assertThrows(Exception.class, () ->
                objectMapper.readValue(json, LocalDateTimeInput.class));
    }
}
