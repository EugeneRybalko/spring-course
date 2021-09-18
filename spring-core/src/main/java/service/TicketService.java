package service;

import dao.TicketDao;
import model.Ticket;

import java.util.List;

public class TicketService {

    private TicketDao ticketDao;

    public void setTicketDao(TicketDao ticketDao) {
        this.ticketDao = ticketDao;
    }

    public List<Ticket> retrieveUserTickets(long userId) {
        return ticketDao.findTicketsByUserId(userId);
    }

    public List<Ticket> retrieveTicketsByEvent(long eventId) {
        return ticketDao.findTicketsByEventId(eventId);
    }

    public Ticket bookTicket(Ticket ticket) {
        return ticketDao.insertTicket(ticket);
    }

    public boolean cancelBooking(long ticketId) {
        return ticketDao.deleteTicket(ticketId);
    }
}
