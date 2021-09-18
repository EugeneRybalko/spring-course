package facade;

import model.Event;
import model.Ticket;
import model.User;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import service.EventService;
import service.TicketService;
import service.UserService;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingFacadeTest {

    @Mock UserService userService;
    @Mock EventService eventService;
    @Mock TicketService ticketService;
    BookingFacade bookingFacade;

    static Ticket testTicket;
    static User testUser;
    static Event testEvent;
    static Date testDate;
    static List<Event> testEventList;
    int pageSize = 10, pageNumber = 1;

    @BeforeAll
    static void beforeAll() {
        testUser = new User();
        testUser.setId(1);
        testUser.setName("Test user");
        testUser.setEmail("test@mail.com");

        testDate = new Date();
        testEvent = new Event();
        testEvent.setId(1);
        testEvent.setTitle("Test Event");
        testEvent.setDate(testDate);
        testEventList = Lists.newArrayList(testEvent);

        testTicket = new Ticket();
        testTicket.setId(1);
        testTicket.setEventId(1);
        testTicket.setUserId(1);
        testTicket.setPlace(1);
        testTicket.setCategory(Ticket.Category.PREMIUM);
    }

    @BeforeEach
    void setUp() {
        bookingFacade = new BookingFacadeImpl(eventService, ticketService, userService);
    }

    @Test
    void getEventById_EventIsRetrieved() {
        when(eventService.retrieveEventById(anyLong())).thenReturn(testEvent);
        assertThat(bookingFacade.getEventById(testEvent.getId())).isNotNull();
    }

    @Test
    void getEventsByTitle_EventsAreRetrieved() {
        when(eventService.retrieveEventByTitle(any(String.class))).thenReturn(testEventList);
        assertThat(bookingFacade.getEventsByTitle(testEvent.getTitle(), pageSize, pageNumber)).isNotEmpty();
    }

    @Test
    void getEventsForDay_EventAreRetrieved() {
        when(eventService.retrieveEventByDay(any(Date.class))).thenReturn(testEventList);
        assertThat(bookingFacade.getEventsForDay(testDate, pageSize, pageNumber)).isNotEmpty();
    }

    @Test
    void createEvent_EventIsCreated() {
        when(eventService.addEvent(any(Event.class))).thenReturn(testEvent);
        assertThat(bookingFacade.createEvent(testEvent)).isEqualTo(testEvent);
    }

    @Test
    void updateEvent_EventIsUpdated() {
        when(eventService.updateEvent(any(Event.class))).thenReturn(testEvent);
        assertThat(bookingFacade.updateEvent(testEvent)).isEqualTo(testEvent);
    }

    @Test
    void deleteEvent_EventIsDeleted() {
        when(eventService.removeEvent(anyLong())).thenReturn(true);
        assertTrue(bookingFacade.deleteEvent(1));
    }

    @Test
    void getUserById_UserIsRetrieved() {
        when(userService.retrieveUserById(anyLong())).thenReturn(testUser);
        assertThat(bookingFacade.getUserById(1)).isEqualTo(testUser);
    }

    @Test
    void getUserByEmail_UserIsRetrieved() {
        when(userService.retrieveUserByEmail(anyString())).thenReturn(testUser);
        assertThat(bookingFacade.getUserByEmail("test@mail.com")).isEqualTo(testUser);
    }

    @Test
    void getUsersByName_UsersAreRetrieved() {
        List<User> expectedUsers = Lists.newArrayList(testUser);
        when(userService.retrieveUserByName(anyString())).thenReturn(expectedUsers);
        assertThat(bookingFacade.getUsersByName("Test user", pageSize, pageNumber)).isEqualTo(expectedUsers);
    }

    @Test
    void createUser_UserIsCreated() {
        when(userService.addUser(any(User.class))).thenReturn(testUser);
        assertThat(bookingFacade.createUser(testUser)).isEqualTo(testUser);
    }

    @Test
    void updateUser_UserIsUpdated() {
        when(userService.updateUser(any(User.class))).thenReturn(testUser);
        assertThat(bookingFacade.updateUser(testUser)).isEqualTo(testUser);
    }

    @Test
    void deleteUser_UserIsDeleted() {
        when(userService.removeUser(anyLong())).thenReturn(true);
        assertTrue(bookingFacade.deleteUser(1));
    }

    @Test
    void bookTicket_TicketIsBooked() {
        when(ticketService.bookTicket(any(Ticket.class))).thenReturn(testTicket);
        assertThat(bookingFacade.bookTicket(1, 1, 1, Ticket.Category.PREMIUM)).isEqualTo(testTicket);
    }

    @Test
    void bookTicket_TicketIsNotBooked_ThrowsException() {
        when(ticketService.retrieveTicketsByEvent(anyLong())).thenReturn(Lists.newArrayList(testTicket));
        assertThrows(IllegalStateException.class, () -> bookingFacade.bookTicket(1, 1, 1, Ticket.Category.PREMIUM));
    }

    @Test
    void getBookedTicketsByEvent_TicketsAreRetrieved() {
        List<Ticket> expectedTickets = Lists.newArrayList(testTicket);
        when(ticketService.retrieveTicketsByEvent(anyLong())).thenReturn(expectedTickets);
        assertThat(bookingFacade.getBookedTickets(testEvent, pageSize, pageNumber)).isEqualTo(expectedTickets);
    }

    @Test
    void getBookedTicketsByUser_TicketsAreRetrieved() {
        List<Ticket> expectedTickets = Lists.newArrayList(testTicket);
        when(ticketService.retrieveUserTickets(anyLong())).thenReturn(expectedTickets);
        assertThat(bookingFacade.getBookedTickets(testUser, pageSize, pageNumber)).isEqualTo(expectedTickets);
    }

    @Test
    void cancelTicket_TicketIsReturned() {
        when(ticketService.cancelBooking(anyLong())).thenReturn(true);
        assertTrue(bookingFacade.cancelTicket(1));
    }
}
