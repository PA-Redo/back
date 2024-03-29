package fr.croixrouge.repository;

import fr.croixrouge.domain.model.ID;
import fr.croixrouge.domain.repository.CRUDRepository;
import fr.croixrouge.model.Event;
import fr.croixrouge.model.EventSession;
import fr.croixrouge.model.EventStats;
import fr.croixrouge.model.EventTimeWindow;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends CRUDRepository<ID, Event> {

    Optional<Event> findByEventIdSessionId(ID eventId, ID sessionId);

    List<Event> findByLocalUnitId(ID localUnitId);

    EventStats findByLocalUnitIdOver12Month(ID localUnitId);

    List<Event> findByLocalUnitIdAndMonth(ID localUnitId, int month, int year);

    void updateEventSession(EventSession eventSession, Event event);
    List<Event> findByLocalUnitIdAndTrimester(ID localUnitId, int month, int year);

    boolean registerParticipant(ID eventId, ID sessionId, ID timeWindowId, ID participantId);

    boolean removeParticipant(ID eventId, ID sessionId, ID timeWindowId, ID participantId);
    void updateEventSession(EventSession event);

    boolean updateSingleEvent(ID eventId, ID sessionId, Event event);

    boolean updateEventSessions(ID eventId, ID sessionId, Event event, int eventTimeWindowDuration, int eventTimeWindowOccurrence, int eventTimeWindowMaxParticipants);

    boolean deleteEventSession(ID eventId, ID sessionId);

    List<Event> findByBeneficiary(ID beneficiaryId);

    List<EventTimeWindow> findEventInTheNext15Minutes();
}
