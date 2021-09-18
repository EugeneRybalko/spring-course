package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Event;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.*;
import java.util.stream.Collectors;

public class EventDao {

    private String filePath;
    private Map<Long, Event> eventStorage;

    private void init() throws IOException {
        Resource resource = new ClassPathResource(filePath);
        if (resource.isFile()) {
            String users = new String(Files.readAllBytes(resource.getFile().toPath()));
            this.eventStorage = new ObjectMapper().readValue(users, new TypeReference<Map<Long, Event>>() {
            });
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Event findEventById(long eventId) {
        return eventStorage.get(eventId);
    }

    public List<Event> findEventByTitle(String title) {
        return eventStorage.values().stream().filter(event -> event.getTitle().equals(title)).collect(Collectors.toList());
    }

    public List<Event> findEventByDay(Date date) {
        return eventStorage.values().stream().filter(event -> {
            Calendar eventCalendar = Calendar.getInstance();
            Calendar expectedEventCalendar = Calendar.getInstance();
            eventCalendar.setTime(event.getDate());
            expectedEventCalendar.setTime(date);
            return eventCalendar.get(Calendar.DAY_OF_YEAR) == expectedEventCalendar.get(Calendar.DAY_OF_YEAR) &&
                    eventCalendar.get(Calendar.YEAR) == expectedEventCalendar.get(Calendar.YEAR);
        }).collect(Collectors.toList());
    }

    public Event insertEvent(Event event) {
        Optional<Event> maxEvent = eventStorage.values().stream().max(Comparator.comparingLong(Event::getId));
        long maxId = 1;
        if (maxEvent.isPresent()) {
            maxId = maxEvent.get().getId() + 1;
        }
        event.setId(maxId);
        eventStorage.put(maxId, event);
        return eventStorage.get(maxId);
    }

    public Event updateEvent(Event event) {
        if (eventStorage.containsKey(event.getId())) {
            eventStorage.put(event.getId(), event);
            return event;
        }
        throw new IllegalStateException("There are no event with id: " + event.getId());
    }

    public boolean deleteEvent(long eventId) {
        return Objects.nonNull(eventStorage.remove(eventId));
    }
}
