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

    @Mock UserService userServiceMock;
    @Mock EventService eventServiceMock;
    @Mock TicketService ticketServiceMock;
    BookingFacade bookingFacade;

    static Ticket testTicket;
    static User testUser;
    static Event testEvent;
    static Date testDate;
    static List<Event> testEventList;
    int pageSize = 10, pageNumber = 1;

    @BeforeAll
    static void beforeAll() {
        testDate = new Date();
        testUser = User.builder().id(1).name("Test user").email("test@mail.com").build();
        testEvent = Event.builder().id(1).title("Test Event").date(testDate).build();
        testTicket = Ticket.builder().id(1).eventId(1).userId(1).place(1).category(Ticket.Category.PREMIUM).build();
        testEventList = Lists.newArrayList(testEvent);
    }

    @BeforeEach
    void setUp() {
        bookingFacade = new BookingFacadeImpl(eventServiceMock, ticketServiceMock, userServiceMock);
    }

    @Test
    void getEventById_EventIsRetrieved() {
        when(eventServiceMock.retrieveEventById(anyLong())).thenReturn(testEvent);
        assertThat(bookingFacade.getEventById(testEvent.getId())).isNotNull();
    }

    @Test
    void getEventsByTitle_EventsAreRetrieved() {
        when(eventServiceMock.retrieveEventByTitle(any(String.class))).thenReturn(testEventList);
        assertThat(bookingFacade.getEventsByTitle(testEvent.getTitle(), pageSize, pageNumber)).isNotEmpty();
    }

    @Test
    void getEventsForDay_EventAreRetrieved() {
        when(eventServiceMock.retrieveEventByDay(any(Date.class))).thenReturn(testEventList);
        assertThat(bookingFacade.getEventsForDay(testDate, pageSize, pageNumber)).isNotEmpty();
    }

    @Test
    void createEvent_EventIsCreated() {
        when(eventServiceMock.addEvent(any(Event.class))).thenReturn(testEvent);
        assertThat(bookingFacade.createEvent(testEvent)).isEqualTo(testEvent);
    }

    @Test
    void updateEvent_EventIsUpdated() {
        when(eventServiceMock.updateEvent(any(Event.class))).thenReturn(testEvent);
        assertThat(bookingFacade.updateEvent(testEvent)).isEqualTo(testEvent);
    }

    @Test
    void deleteEvent_EventIsDeleted() {
        when(eventServiceMock.removeEvent(anyLong())).thenReturn(true);
        assertTrue(bookingFacade.deleteEvent(1));
    }

    @Test
    void getUserById_UserIsRetrieved() {
        when(userServiceMock.retrieveUserById(anyLong())).thenReturn(testUser);
        assertThat(bookingFacade.getUserById(1)).isEqualTo(testUser);
    }

    @Test
    void getUserByEmail_UserIsRetrieved() {
        when(userServiceMock.retrieveUserByEmail(anyString())).thenReturn(testUser);
        assertThat(bookingFacade.getUserByEmail("test@mail.com")).isEqualTo(testUser);
    }

    @Test
    void getUsersByName_UsersAreRetrieved() {
        List<User> expectedUsers = Lists.newArrayList(testUser);
        when(userServiceMock.retrieveUserByName(anyString())).thenReturn(expectedUsers);
        assertThat(bookingFacade.getUsersByName("Test user", pageSize, pageNumber)).isEqualTo(expectedUsers);
    }

    @Test
    void createUser_UserIsCreated() {
        when(userServiceMock.addUser(any(User.class))).thenReturn(testUser);
        assertThat(bookingFacade.createUser(testUser)).isEqualTo(testUser);
    }

    @Test
    void updateUser_UserIsUpdated() {
        when(userServiceMock.updateUser(any(User.class))).thenReturn(testUser);
        assertThat(bookingFacade.updateUser(testUser)).isEqualTo(testUser);
    }

    @Test
    void deleteUser_UserIsDeleted() {
        when(userServiceMock.removeUser(anyLong())).thenReturn(true);
        assertTrue(bookingFacade.deleteUser(1));
    }

    @Test
    void bookTicket_TicketIsBooked() {
        when(ticketServiceMock.bookTicket(any(Ticket.class))).thenReturn(testTicket);
        assertThat(bookingFacade.bookTicket(1, 1, 1, Ticket.Category.PREMIUM)).isEqualTo(testTicket);
    }

    @Test
    void bookTicket_TicketIsNotBooked_ThrowsException() {
        when(ticketServiceMock.retrieveTicketsByEvent(anyLong())).thenReturn(Lists.newArrayList(testTicket));
        assertThrows(IllegalStateException.class, () -> bookingFacade.bookTicket(1, 1, 1, Ticket.Category.PREMIUM));
    }

    @Test
    void getBookedTicketsByEvent_TicketsAreRetrieved() {
        List<Ticket> expectedTickets = Lists.newArrayList(testTicket);
        when(ticketServiceMock.retrieveTicketsByEvent(anyLong())).thenReturn(expectedTickets);
        assertThat(bookingFacade.getBookedTickets(testEvent, pageSize, pageNumber)).isEqualTo(expectedTickets);
    }

    @Test
    void getBookedTicketsByUser_TicketsAreRetrieved() {
        List<Ticket> expectedTickets = Lists.newArrayList(testTicket);
        when(ticketServiceMock.retrieveUserTickets(anyLong())).thenReturn(expectedTickets);
        assertThat(bookingFacade.getBookedTickets(testUser, pageSize, pageNumber)).isEqualTo(expectedTickets);
    }

    @Test
    void cancelTicket_TicketIsReturned() {
        when(ticketServiceMock.cancelBooking(anyLong())).thenReturn(true);
        assertTrue(bookingFacade.cancelTicket(1));
    }
}
