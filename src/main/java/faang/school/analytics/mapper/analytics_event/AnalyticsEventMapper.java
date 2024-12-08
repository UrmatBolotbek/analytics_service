package faang.school.analytics.mapper.analytics_event;

import faang.school.analytics.event.FundRaisedEvent;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.event.AnalyticsEventResponseDto;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.model.AnalyticsEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AnalyticsEventMapper {

    AnalyticsEventResponseDto toDto(AnalyticsEvent analyticsEvent);

    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "donatedAt", target = "receivedAt")
    AnalyticsEvent toEntity(FundRaisedEvent fundRaisedEvent);

    @Mapping(source = "projectId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "eventTime", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(ProjectViewEvent projectViewEvent);

    @Mapping(source = "goalId", target = "receiverId")
    @Mapping(source = "userId", target = "actorId")
    @Mapping(source = "completedAt", target = "receivedAt")
    AnalyticsEvent toAnalyticsEvent(GoalCompletedEvent goalCompletedEvent);
}
