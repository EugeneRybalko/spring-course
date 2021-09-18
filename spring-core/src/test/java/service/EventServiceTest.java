package service;

import dao.EventDao;
import model.Event;
import org.assertj.core.api.Assertions;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    @Mock EventDao eventDao;
    EventService eventService;

    static Event testEvent;

    @BeforeAll
    static void beforeAll() {
        testEvent = new Event();
        testEvent.setId(1);
        testEvent.setTitle("Test Event");
        testEvent.setDate(new Date());
    }

    @BeforeEach
    void setUp() {
        eventService.setEventDao(eventDao);
    }

    @Test
    void retrieveEventById_EventIsRetrieved() {
        when(eventDao.findEventById(anyLong())).thenReturn(testEvent);
        assertThat(eventService.retrieveEventById(1)).isEqualTo(testEvent);
    }

    @Test
    void retrieveEventByTitle_EventIsRetrieved() {
        when(eventDao.findEventByTitle(anyString())).thenReturn(Lists.newArrayList(testEvent));
        assertThat(eventService.retrieveEventByTitle("Test Event")).isNotEmpty();
    }

    @Test
    void retrieveEventByDay_EventIsRetrieved() {
        when(eventDao.findEventByDay(any(Date.class))).thenReturn(Lists.newArrayList(testEvent));
        assertThat(eventService.retrieveEventByDay(new Date())).isNotEmpty();
    }

    @Test
    void addEvent_EventIsAdded() {
    }

    @Test
    void updateEvent_EventIsUpdated() {
    }

    @Test
    void removeEvent_EventIsRemoved() {
    }
}