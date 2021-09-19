package service;

import dao.TicketDao;
import model.Ticket;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketServiceTest {

    @Mock TicketDao ticketDao;
    TicketService ticketService;

    static Ticket testTicket;

    @BeforeAll
    static void beforeAll() {
        testTicket = new Ticket();
        testTicket.setId(1);
        testTicket.setPlace(1);
        testTicket.setCategory(Ticket.Category.PREMIUM);
        testTicket.setEventId(1);
        testTicket.setUserId(1);
    }

    @BeforeEach
    void setUp() {
        ticketService = new TicketService();
        ticketService.setTicketDao(ticketDao);
    }

    @Test
    void retrieveUserTickets_TicketsAreRetrieved() {
        when(ticketDao.findTicketsByUserId(anyLong())).thenReturn(Lists.newArrayList(testTicket));
        assertThat(ticketService.retrieveUserTickets(1)).isNotEmpty();
    }

    @Test
    void retrieveTicketsByEvent_TicketsAreRetrieved() {
        when(ticketDao.findTicketsByEventId(anyLong())).thenReturn(Lists.newArrayList(testTicket));
        assertThat(ticketService.retrieveTicketsByEvent(1)).isNotEmpty();
    }

    @Test
    void bookTicket_TicketIsBooked() {
        when(ticketDao.insertTicket(any(Ticket.class))).thenReturn(testTicket);
        assertThat(ticketService.bookTicket(testTicket)).isEqualTo(testTicket);
    }

    @Test
    void cancelBooking_TicketIsCanceled() {
        when(ticketDao.deleteTicket(anyLong())).thenReturn(true);
        assertTrue(ticketService.cancelBooking(1));
    }
}