package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PremiumBoughtEvent {
    private long userId;
    private BigDecimal amount;
    private int days;
    private LocalDateTime purchaseDate;
}
