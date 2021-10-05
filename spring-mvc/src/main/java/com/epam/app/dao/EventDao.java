package com.epam.app.dao;

import com.epam.app.model.Event;
import com.epam.app.storage.EventStorage;

import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventDao {

    private EventStorage eventStorage;

    public void setEventStorage(EventStorage eventStorage) {
        this.eventStorage = eventStorage;
    }

    public Event findEventById(long eventId) {
        return eventStorage.getMap().get(eventId);
    }

    public List<Event> findEventByTitle(String title) {
        return eventStorage.getMap().values().stream().filter(event -> event.getTitle().equals(title)).collect(Collectors.toList());
    }

    public List<Event> findEventByDay(Date date) {
        return eventStorage.getMap().values().stream().filter(event -> {
            Calendar eventCalendar = Calendar.getInstance();
            Calendar expectedEventCalendar = Calendar.getInstance();
            eventCalendar.setTime(event.getDate());
            expectedEventCalendar.setTime(date);
            return eventCalendar.get(Calendar.DAY_OF_YEAR) == expectedEventCalendar.get(Calendar.DAY_OF_YEAR) &&
                    eventCalendar.get(Calendar.YEAR) == expectedEventCalendar.get(Calendar.YEAR);
        }).collect(Collectors.toList());
    }

    public Event insertEvent(Event event) {
        Optional<Event> maxEvent = eventStorage.getMap().values().stream().max(Comparator.comparingLong(Event::getId));
        long maxId = 1;
        if (maxEvent.isPresent()) {
            maxId = maxEvent.get().getId() + 1;
        }
        event.setId(maxId);
        eventStorage.getMap().put(maxId, event);
        return eventStorage.getMap().get(maxId);
    }

    public Event updateEvent(Event event) {
        if (eventStorage.getMap().containsKey(event.getId())) {
            eventStorage.getMap().put(event.getId(), event);
            return event;
        }
        throw new IllegalStateException("There are no event with id: " + event.getId());
    }

    public boolean deleteEvent(long eventId) {
        return Objects.nonNull(eventStorage.getMap().remove(eventId));
    }
}
