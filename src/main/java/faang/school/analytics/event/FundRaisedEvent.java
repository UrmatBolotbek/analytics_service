package faang.school.analytics.event;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class FundRaisedEvent {
    private long userId;
    private long projectId;
    private BigDecimal amount;
    private LocalDateTime donatedAt;
}
