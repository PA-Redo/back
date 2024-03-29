package fr.croixrouge.exposition.dto.event;

import fr.croixrouge.model.EventStats;

public class EventStatsResponse {
    private int numberOfEventsOverTheMonth;
    private int totalParticipantsOverTheMonth;
    private int numberOfEventsOverTheYear;
    private int totalParticipantsOverTheYear;

    public EventStatsResponse() {
    }

    public EventStatsResponse(int numberOfEventsOverTheMonth, int totalParticipantsOverTheMonth, int numberOfEventsOverTheYear, int totalParticipantsOverTheYear) {
        this.numberOfEventsOverTheMonth = numberOfEventsOverTheMonth;
        this.totalParticipantsOverTheMonth = totalParticipantsOverTheMonth;
        this.numberOfEventsOverTheYear = numberOfEventsOverTheYear;
        this.totalParticipantsOverTheYear = totalParticipantsOverTheYear;
    }

    public static EventStatsResponse fromEventStats(EventStats eventStats) {
        return new EventStatsResponse(eventStats.getNumberOfEventsOverTheMonth(), eventStats.getTotalParticipantsOverTheMonth(), eventStats.getNumberOfEventsOverTheYear(), eventStats.getTotalParticipantsOverTheYear());
    }

    public int getNumberOfEventsOverTheMonth() {
        return numberOfEventsOverTheMonth;
    }

    public int getTotalParticipantsOverTheMonth() {
        return totalParticipantsOverTheMonth;
    }

    public int getNumberOfEventsOverTheYear() {
        return numberOfEventsOverTheYear;
    }

    public int getTotalParticipantsOverTheYear() {
        return totalParticipantsOverTheYear;
    }
}
