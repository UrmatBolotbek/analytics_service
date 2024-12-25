package faang.school.analytics.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class AdBoughtEvent {
    private Long postId;
    private Long userId;
    private Double paymentAmount;
    private Integer duration;
    private LocalDateTime purchaseTime;
    private Long ownerId;
}