package faang.school.analytics.config.redis;

import faang.school.analytics.listener.GoalCompletedEventListener;
import faang.school.analytics.listener.project.ProjectViewEventListener;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@RequiredArgsConstructor
@Configuration
public class RedisConfig {

    private final GoalCompletedEventListener goalCompletedEventListener;
    private final ProjectViewEventListener projectViewEventListener;

    @Value("${spring.data.redis.host}")
    private String redisHost;
    @Value("${spring.data.redis.port}")
    private int redisPort;
    @Value("${spring.data.redis.channel.project-view-channel}")
    private String topicProjectView;
    @Value("${spring.data.redis.channel.goal-completed}")
    private String topicGoalCompleted;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisConfig = new RedisStandaloneConfiguration(redisHost, redisPort);
        return new JedisConnectionFactory(redisConfig);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        MessageListenerAdapter projectViewListener = getListenerAdapter(projectViewEventListener);
        container.addMessageListener(projectViewListener, new ChannelTopic(topicProjectView));
        MessageListenerAdapter goalCompletedListener = getListenerAdapter(goalCompletedEventListener);
        container.addMessageListener(goalCompletedListener, new ChannelTopic(topicGoalCompleted));
        return container;
    }

    private MessageListenerAdapter getListenerAdapter(MessageListener listenerAdapter) {
        return new MessageListenerAdapter(listenerAdapter);
    }

}
