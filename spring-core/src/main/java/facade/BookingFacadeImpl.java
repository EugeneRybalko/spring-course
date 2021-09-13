package facade;

import model.Event;
import model.Ticket;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.EventService;
import service.TicketService;
import service.UserService;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public final class BookingFacadeImpl implements BookingFacade {

    private static final Logger LOG = LoggerFactory.getLogger(BookingFacadeImpl.class);

    private final EventService eventService;
    private final TicketService ticketService;
    private final UserService userService;

    public BookingFacadeImpl(EventService eventService, TicketService ticketService, UserService userService) {
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.userService = userService;
    }

    public Event getEventById(long eventId) {
        return eventService.retrieveEventById(eventId);
    }

    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        checkPageSize(pageSize, pageNum);
        final List<Event> events = eventService.retrieveEventByTitle(title);
        LOG.debug("Events: " + events);
        return events.isEmpty() ? Collections.emptyList() : listByPage(events, pageSize, pageNum);
    }

    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        checkPageSize(pageSize, pageNum);
        final List<Event> events = eventService.retrieveEventByDay(day);
        LOG.debug("Events: " + events);
        return events.isEmpty() ? Collections.emptyList() : listByPage(events, pageSize, pageNum);
    }

    public Event createEvent(Event event) {
        return eventService.addEvent(event);
    }

    public Event updateEvent(Event event) {
        return eventService.updateEvent(event);
    }

    public boolean deleteEvent(long eventId) {
        return eventService.removeEvent(eventId);
    }

    public User getUserById(long userId) {
        return userService.retrieveUserById(userId);
    }

    public User getUserByEmail(String email) {
        return userService.retrieveUserByEmail(email);
    }

    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        checkPageSize(pageSize, pageNum);
        final List<User> users = userService.retrieveUserByName(name);
        LOG.debug("Users: " + users);
        return users.isEmpty() ? Collections.emptyList() : listByPage(users, pageSize, pageNum);
    }

    public User createUser(User user) {
        return userService.addUser(user);
    }

    public User updateUser(User user) {
        return userService.updateUser(user);
    }

    public boolean deleteUser(long userId) {
        return userService.removeUser(userId);
    }

    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        List<Ticket> bookedTickets = getBookedTickets(eventId);
        final Optional<Ticket> bookedTicket = bookedTickets.stream()
                .filter(ticket -> place == ticket.getPlace() && category.equals(ticket.getCategory()) && userId == ticket.getUserId()).findFirst();
        if (bookedTicket.isPresent()) {
            LOG.error("User is already preset: " + bookedTicket.get());
            throw new IllegalStateException("Place already booked");
        }
        Ticket ticket = new Ticket();
        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setPlace(place);
        ticket.setCategory(category);
        return ticketService.bookTicket(ticket);
    }

    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        checkPageSize(pageSize, pageNum);
        List<Ticket> tickets = ticketService.retrieveUserTickets(user.getId());
        LOG.debug("Tickets: " + tickets);
        tickets.sort(ticketComparatorByEventDate().reversed());
        LOG.debug("Sorted tickets: " + tickets);
        return listByPage(tickets, pageSize, pageNum);
    }

    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        checkPageSize(pageSize, pageNum);
        List<Ticket> tickets = getBookedTickets(event.getId());
        LOG.debug("Tickets: " + tickets);
        tickets.sort(ticketComparatorByUserEmail());
        LOG.debug("Sorted tickets: " + tickets);
        return listByPage(tickets, pageSize, pageNum);
    }

    private List<Ticket> getBookedTickets(long eventId) {
        return ticketService.retrieveTicketsByEvent(eventId);
    }

    public boolean cancelTicket(long ticketId) {
        return ticketService.cancelBooking(ticketId);
    }

    private Comparator<Ticket> ticketComparatorByEventDate() {
        return Comparator.comparing(o -> eventService.retrieveEventById(o.getEventId()).getDate());
    }

    private Comparator<Ticket> ticketComparatorByUserEmail() {
        return Comparator.comparing(o -> userService.retrieveUserById(o.getUserId()).getEmail());
    }

    private void checkPageSize(int pageSize, int pageNum) {
        if (pageSize <= 0 || pageNum <= 0) {
            throw new IllegalArgumentException("invalid page size: " + pageSize);
        }
    }

    private <T> List<T> listByPage(List<T> list, int pageSize, int pageNum) {
        final int fromIndex = (pageNum - 1) * pageSize;
        return list.subList(fromIndex, Math.min(fromIndex + pageSize, list.size()));
    }
}
