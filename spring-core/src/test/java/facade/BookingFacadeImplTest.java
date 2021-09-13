package facade;

import model.Event;
import model.Ticket;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Objects;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:beans.xml")
public class BookingFacadeImplTest {

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    public void Should_Pass_When_UserIsRetrieved() {
        final User user = bookingFacade.getUserById(1);
        Assertions.assertTrue(Objects.nonNull(user));
    }

    @Test
    public void Should_Pass_When_TicketsByUserIsRetrieved() {
        final User user = bookingFacade.getUserById(1);
        final int pageSize = 1, pageNumber = 1;
        final List<Ticket> tickets = bookingFacade.getBookedTickets(user, pageSize, pageNumber);
        Assertions.assertFalse(tickets.isEmpty());
    }

    @Test
    public void Should_Pass_When_TicketsByEventIsRetrieved() {
        final Event event = bookingFacade.getEventById(1);
        final int pageSize = 1, pageNumber = 1;
        final List<Ticket> tickets = bookingFacade.getBookedTickets(event, pageSize, pageNumber);
        Assertions.assertFalse(tickets.isEmpty());
    }

    @Test
    public void Should_Pass_When_EventIsRetrieved() {
        final Event event = bookingFacade.getEventById(1);
        Assertions.assertTrue(Objects.nonNull(event));
    }

    @Test
    public void Should_Pass_When_EventByTitleIsRetrieved() {
        final int pageSize = 1, pageNumber = 1;
        final List<Event> events = bookingFacade.getEventsByTitle("Event1", pageSize, pageNumber);
        Assertions.assertFalse(events.isEmpty());
    }

    @Test
    public void Should_Pass_When_EventByTitleIsNotRetrieved() {
        final int pageSize = 1, pageNumber = 1;
        final List<Event> events = bookingFacade.getEventsByTitle("Event10", pageSize, pageNumber);
        Assertions.assertTrue(events.isEmpty());
    }

    @Test
    public void Should_Pass_When_EventByDayIsRetrieved() {
        final int pageSize = 1, pageNumber = 1;
        final Calendar calendar = new GregorianCalendar(2021, Calendar.MAY, 27);
        final List<Event> events = bookingFacade.getEventsForDay(calendar.getTime(), pageSize, pageNumber);
        Assertions.assertFalse(events.isEmpty());
    }

    @Test
    public void Should_Pass_When_EventByDayIsNotRetrieved() {
        final int pageSize = 1, pageNumber = 1;
        final Calendar calendar = new GregorianCalendar(2021, Calendar.JUNE, 27);
        final List<Event> events = bookingFacade.getEventsForDay(calendar.getTime(), pageSize, pageNumber);
        Assertions.assertTrue(events.isEmpty());
    }

    @Test
    public void Should_Pass_When_EventIsAdded() {
        final Event event = new Event();
        event.setDate(new Date());
        event.setTitle("New Event");
        Assertions.assertTrue(Objects.nonNull(bookingFacade.createEvent(event)));
    }

    @Test
    public void Should_Pass_When_EventIsUpdated() {
        final String initTitle = "New Event To Update";
        final Event event = new Event();
        event.setDate(new Date());
        event.setTitle(initTitle);
        Event updatedEvent = bookingFacade.createEvent(event);
        updatedEvent.setTitle("New Event Title");
        updatedEvent = bookingFacade.updateEvent(updatedEvent);
        Assertions.assertNotEquals(initTitle, updatedEvent.getTitle());
    }

    @Test
    public void Should_Pass_When_EventIsDeleted() {
        final String initTitle = "New Event To Delete";
        final Event event = new Event();
        event.setDate(new Date());
        event.setTitle(initTitle);
        bookingFacade.createEvent(event);
        Assertions.assertTrue(bookingFacade.deleteEvent(event.getId()));
    }

    @Test
    public void Should_Pass_When_UserByEmailIsRetrieved() {
        Assertions.assertTrue(Objects.nonNull(bookingFacade.getUserByEmail("dima@gmail.com")));
    }

    @Test
    public void Should_Pass_When_UserByNameIsRetrieved() {
        final int pageSize = 1, pageNumber = 1;
        Assertions.assertFalse(bookingFacade.getUsersByName("Dima", pageSize, pageNumber).isEmpty());
    }

    @Test
    public void Should_Pass_When_UserIsCreated() {
        final User user = new User();
        user.setEmail("testUser@gmail.com");
        user.setName("testUser");
        bookingFacade.createUser(user);
        Assertions.assertTrue(Objects.nonNull(bookingFacade.getUserById(user.getId())));
    }

    @Test
    public void Should_Pass_When_UserIsUpdated() {
        final String initName = "testName";
        final User user = new User();
        user.setName(initName);
        user.setEmail("some@gmail.com");
        bookingFacade.createUser(user);
        user.setName("newTest");
        bookingFacade.updateUser(user);
        Assertions.assertNotEquals(initName, user.getName());
    }

    @Test
    public void Should_Pass_When_UserIsDeleted() {
        final User user = new User();
        user.setName("Test name");
        user.setEmail("someUser@gmail.com");
        bookingFacade.createUser(user);
        Assertions.assertTrue(bookingFacade.deleteUser(user.getId()));
    }
}
