package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.Ticket;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public final class TicketDao {

    private String filePath;
    private Map<Long, Ticket> ticketStorage;

    private void init() throws IOException {
        final Resource resource = new ClassPathResource(filePath);
        if (resource.isFile()) {
            String users = new String(Files.readAllBytes(resource.getFile().toPath()));
            this.ticketStorage = new ObjectMapper().readValue(users, new TypeReference<Map<Long, Ticket>>() {});
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public List<Ticket> findTicketsByUserId(long userId) {
        return ticketStorage.values().stream().filter(ticket -> ticket.getUserId() == userId).collect(Collectors.toList());
    }

    public List<Ticket> findTicketsByEventId(long eventId) {
        return ticketStorage.values().stream().filter(ticket -> ticket.getEventId() == eventId).collect(Collectors.toList());
    }

    public Ticket insertTicket(Ticket ticket) {
        final Optional<Ticket> maxTicket = ticketStorage.values().stream().max(Comparator.comparingLong(Ticket::getId));
        long maxId = 1;
        if (maxTicket.isPresent()) {
            maxId = maxTicket.get().getId() + 1;
        }
        ticket.setId(maxId);
        ticketStorage.put(maxId, ticket);
        return ticketStorage.get(maxId);
    }

    public boolean deleteTicket(long ticketId) {
        return Objects.nonNull(ticketStorage.remove(ticketId));
    }
}
