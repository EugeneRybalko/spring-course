package com.epam.app.service;

import com.epam.app.dao.EventDao;
import com.epam.app.model.Event;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock EventDao eventDaoMock;
    EventService eventService;

    static Event testEvent;

    @BeforeAll
    static void beforeAll() {
        testEvent = Event.builder().id(1).title("Test Event").date(new Date()).build();
    }

    @BeforeEach
    void setUp() {
        eventService = new EventService();
        eventService.setEventDao(eventDaoMock);
    }

    @Test
    void retrieveEventById_EventIsRetrieved() {
        when(eventDaoMock.findEventById(anyLong())).thenReturn(testEvent);
        assertThat(eventService.retrieveEventById(1)).isEqualTo(testEvent);
    }

    @Test
    void retrieveEventByTitle_EventIsRetrieved() {
        when(eventDaoMock.findEventByTitle(anyString())).thenReturn(Lists.newArrayList(testEvent));
        assertThat(eventService.retrieveEventByTitle("Test Event")).isNotEmpty();
    }

    @Test
    void retrieveEventByDay_EventIsRetrieved() {
        when(eventDaoMock.findEventByDay(any(Date.class))).thenReturn(Lists.newArrayList(testEvent));
        assertThat(eventService.retrieveEventByDay(new Date())).isNotEmpty();
    }

    @Test
    void addEvent_EventIsAdded() {
        when(eventDaoMock.insertEvent(any(Event.class))).thenReturn(testEvent);
        assertThat(eventService.addEvent(testEvent)).isEqualTo(testEvent);
    }

    @Test
    void updateEvent_EventIsUpdated() {
        when(eventDaoMock.updateEvent(any(Event.class))).thenReturn(testEvent);
        assertThat(eventService.updateEvent(testEvent)).isEqualTo(testEvent);
    }

    @Test
    void removeEvent_EventIsRemoved() {
        when(eventDaoMock.deleteEvent(anyLong())).thenReturn(true);
        assertTrue(eventService.removeEvent(1));
    }
}