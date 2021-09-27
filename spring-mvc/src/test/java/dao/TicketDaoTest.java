package dao;

import model.Ticket;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import storage.TicketStorage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TicketDaoTest {

    @Mock TicketStorage ticketStorageMock;
    TicketDao ticketDao;

    static Ticket testTicket;

    @BeforeAll
    static void beforeAll() {
        testTicket = Ticket.builder().id(1).userId(1).eventId(1).place(10).category(Ticket.Category.PREMIUM).build();
    }

    @BeforeEach
    void setUp() {
        ticketDao = new TicketDao();
        ticketDao.setTicketStorage(ticketStorageMock);
        when(ticketStorageMock.getMap()).thenReturn(Maps.newHashMap(testTicket.getId(), testTicket));
    }

    @Test
    void findTicketsByUserId_TicketIsRetrieved() {
        assertThat(ticketDao.findTicketsByUserId(1)).isNotEmpty();
    }

    @Test
    void findTicketsByEventId_TicketIsRetrieved() {
        assertThat(ticketDao.findTicketsByEventId(1)).isNotEmpty();
    }

    @Test
    void insertTicket_TicketIsInserted() {
        Ticket expectedTicket = Ticket.builder().userId(2).eventId(1).place(112).category(Ticket.Category.PREMIUM).build();
        assertThat(ticketDao.insertTicket(expectedTicket)).isEqualTo(expectedTicket);
    }

    @Test
    void deleteTicket_TicketIsDeleted() {
        Ticket expectedTicket = Ticket.builder().userId(3).eventId(1).place(100).category(Ticket.Category.PREMIUM).build();
        ticketDao.insertTicket(expectedTicket);
        assertTrue(ticketDao.deleteTicket(expectedTicket.getId()));
    }
}