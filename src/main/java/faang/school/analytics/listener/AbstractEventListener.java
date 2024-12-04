package faang.school.analytics.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.connection.Message;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.function.Consumer;

@RequiredArgsConstructor
public abstract class AbstractEventListener<T> {

    private final ObjectMapper objectMapper;

    protected void handleEvent(Message message, Class<T> clazz, Consumer<T> consumer) {
        try {
            T event = objectMapper.readValue(message.getBody(), clazz);
            consumer.accept(event);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
