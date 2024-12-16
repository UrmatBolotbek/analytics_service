package faang.school.analytics.event;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CommentEvent {
    private Long postAuthorId;
    private Long commentAuthorId;
    private Long postId;
    private Long commentId;
    private String commentContent;
    private LocalDateTime commentedAt;
}