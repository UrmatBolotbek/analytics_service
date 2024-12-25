package faang.school.analytics.model;

import faang.school.analytics.event.AdBoughtEvent;
import faang.school.analytics.event.FundRaisedEvent;
import faang.school.analytics.event.ProjectViewEvent;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import faang.school.analytics.event.CommentEvent;
import faang.school.analytics.event.FundRaisedEvent;
import faang.school.analytics.event.PremiumBoughtEvent;
import faang.school.analytics.event.ProjectViewEvent;
import faang.school.analytics.event.GoalCompletedEvent;
import faang.school.analytics.event.SearchAppearanceEvent;

public enum EventType {
    PROFILE_VIEW,
    PROJECT_VIEW,
    FOLLOWER,
    POST_PUBLISHED,
    POST_VIEW,
    POST_LIKE,
    POST_COMMENT,
    SKILL_RECEIVED,
    RECOMMENDATION_RECEIVED,
    ADDED_TO_FAVOURITES,
    PROJECT_INVITE,
    TASK_COMPLETED,
    GOAL_COMPLETED,
    ACHIEVEMENT_RECEIVED,
    PROFILE_APPEARED_IN_SEARCH,
    PROJECT_APPEARED_IN_SEARCH,
    FUND_RAISED,
    AD_BOUGHT,
    PREMIUM_BOUGHT;

    private static final Map<Class<?>, EventType> classToEventTypeMap = new HashMap<>();

    static {
        classToEventTypeMap.put(FundRaisedEvent.class, FUND_RAISED);
        classToEventTypeMap.put(GoalCompletedEvent.class, GOAL_COMPLETED);
        classToEventTypeMap.put(ProjectViewEvent.class, PROJECT_VIEW);
        classToEventTypeMap.put(SearchAppearanceEvent.class, PROFILE_APPEARED_IN_SEARCH);
        classToEventTypeMap.put(PremiumBoughtEvent.class, PREMIUM_BOUGHT);
        classToEventTypeMap.put(CommentEvent.class, POST_COMMENT);
        classToEventTypeMap.put(AdBoughtEvent.class, AD_BOUGHT);
    }

    public static EventType fromEventClass(Class<?> clazz) {
        EventType eventType = classToEventTypeMap.get(clazz);
        if (eventType == null) {
            throw new IllegalArgumentException("Unknown event class: " + clazz);
        }
        return eventType;
    }

    public static EventType fromString(String eventTypeString) {
        try {
            return valueOf(eventTypeString.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid event type: " + eventTypeString);
        }
    }

    public static EventType of(int type) {
        for (EventType eventType : EventType.values()) {
            if (eventType.ordinal() == type) {
                return eventType;
            }
        }
        throw new IllegalArgumentException("Unknown event type: " + type);
    }
}
