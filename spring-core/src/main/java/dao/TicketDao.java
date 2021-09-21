package dao;

import model.Ticket;
import storage.TicketStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class TicketDao {
    
    private TicketStorage ticketStorage;

    public void setTicketStorage(TicketStorage ticketStorage) {
        this.ticketStorage = ticketStorage;
    }

    public List<Ticket> findTicketsByUserId(long userId) {
        return ticketStorage.getMap().values().stream().filter(ticket -> ticket.getUserId() == userId).collect(Collectors.toList());
    }

    public List<Ticket> findTicketsByEventId(long eventId) {
        return ticketStorage.getMap().values().stream().filter(ticket -> ticket.getEventId() == eventId).collect(Collectors.toList());
    }

    public Ticket insertTicket(Ticket ticket) {
        final Optional<Ticket> maxTicket = ticketStorage.getMap().values().stream().max(Comparator.comparingLong(Ticket::getId));
        long maxId = 1;
        if (maxTicket.isPresent()) {
            maxId = maxTicket.get().getId() + 1;
        }
        ticket.setId(maxId);
        ticketStorage.getMap().put(maxId, ticket);
        return ticketStorage.getMap().get(maxId);
    }

    public boolean deleteTicket(long ticketId) {
        return Objects.nonNull(ticketStorage.getMap().remove(ticketId));
    }
}
