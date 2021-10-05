package com.epam.app.dao;

import com.epam.app.model.Event;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.epam.app.storage.EventStorage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventDaoTest {

    @Mock EventStorage eventStorageMock;
    EventDao eventDao;

    static Map<Long, Event> eventMap;
    static Event testEvent;

    @BeforeAll
    static void beforeAll() {
        eventMap = new HashMap<>();
        testEvent = Event.builder().id(1).title("Test Title").date(new Date()).build();
        eventMap.put(testEvent.getId(), testEvent);
    }

    @BeforeEach
    void setUp() {
        eventDao = new EventDao();
        eventDao.setEventStorage(eventStorageMock);
        when(eventStorageMock.getMap()).thenReturn(eventMap);
    }

    @Test
    void findEventById_EventIsRetrieved() {
        assertThat(eventDao.findEventById(1)).isEqualTo(testEvent);
    }

    @Test
    void findEventByTitle_EventIsRetrieved() {
        assertThat(eventDao.findEventByTitle("Test Title")).isNotEmpty();
    }

    @Test
    void findEventByDay_EventIsRetrieved() {
        assertThat(eventDao.findEventByDay(testEvent.getDate())).isNotEmpty();
    }

    @Test
    void insertEvent_EventIsInserted() {
        Event expectedEvent = Event.builder().date(new Date()).title("Test").build();
        assertThat(eventDao.insertEvent(expectedEvent)).isEqualTo(expectedEvent);
    }

    @Test
    void updateEvent_EventIsUpdated() {
        Event expectedEvent = Event.builder().title("Test").date(new Date()).build();
        eventDao.insertEvent(expectedEvent);
        expectedEvent.setTitle("New Title");
        assertThat(eventDao.updateEvent(expectedEvent)).isEqualTo(expectedEvent);
    }

    @Test
    void deleteEvent_EventIsDeleted() {
        Event expectedEvent = Event.builder().title("Test").date(new Date()).build();
        eventDao.insertEvent(expectedEvent);
        expectedEvent.setTitle("New Title");
        assertTrue(eventDao.deleteEvent(expectedEvent.getId()));
    }
}