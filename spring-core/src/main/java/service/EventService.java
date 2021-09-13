package service;

import dao.EventDao;
import model.Event;

import java.util.Date;
import java.util.List;

public final class EventService {

    private EventDao eventDao;

    public void setEventDao(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public Event retrieveEventById(long eventId) {
        return eventDao.findEventById(eventId);
    }

    public List<Event> retrieveEventByTitle(String title) {
        return eventDao.findEventByTitle(title);
    }

    public List<Event> retrieveEventByDay(Date date) {
        return eventDao.findEventByDay(date);
    }

    public Event addEvent(Event event) {
        return eventDao.insertEvent(event);
    }

    public Event updateEvent(Event event) {
        return eventDao.updateEvent(event);
    }

    public boolean removeEvent(long eventId) {
        return eventDao.deleteEvent(eventId);
    }
}
