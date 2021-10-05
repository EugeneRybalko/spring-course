package com.epam.app.service;

import com.epam.app.dao.TicketDao;
import com.epam.app.model.Ticket;
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

    @Mock TicketDao ticketDaoMock;
    TicketService ticketService;

    static Ticket testTicket;

    @BeforeAll
    static void beforeAll() {
        testTicket = Ticket.builder().id(1).place(1).category(Ticket.Category.PREMIUM).eventId(1).userId(1).build();
    }

    @BeforeEach
    void setUp() {
        ticketService = new TicketService();
        ticketService.setTicketDao(ticketDaoMock);
    }

    @Test
    void retrieveUserTickets_TicketsAreRetrieved() {
        when(ticketDaoMock.findTicketsByUserId(anyLong())).thenReturn(Lists.newArrayList(testTicket));
        assertThat(ticketService.retrieveUserTickets(1)).isNotEmpty();
    }

    @Test
    void retrieveTicketsByEvent_TicketsAreRetrieved() {
        when(ticketDaoMock.findTicketsByEventId(anyLong())).thenReturn(Lists.newArrayList(testTicket));
        assertThat(ticketService.retrieveTicketsByEvent(1)).isNotEmpty();
    }

    @Test
    void bookTicket_TicketIsBooked() {
        when(ticketDaoMock.insertTicket(any(Ticket.class))).thenReturn(testTicket);
        assertThat(ticketService.bookTicket(testTicket)).isEqualTo(testTicket);
    }

    @Test
    void cancelBooking_TicketIsCanceled() {
        when(ticketDaoMock.deleteTicket(anyLong())).thenReturn(true);
        assertTrue(ticketService.cancelBooking(1));
    }
}