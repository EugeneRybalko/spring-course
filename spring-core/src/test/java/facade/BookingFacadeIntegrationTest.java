package facade;

import model.Event;
import model.Ticket;
import model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Objects;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(locations = "classpath:beans.xml")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class BookingFacadeIntegrationTest {

    private static final String TEST_USER_EMAIL = "yemail@gmail.com";

    @Autowired
    private BookingFacade bookingFacade;

    @Test
    @Order(1)
    public void Should_Pass_When_UserIsCreated() {
        final User user = new User();
        user.setName("Yevhenii");
        user.setEmail(TEST_USER_EMAIL);
        bookingFacade.createUser(user);
        Assertions.assertTrue(user.getId() != 0);
    }

    @Test
    @Order(2)
    public void Should_Pass_When_UserBookedTicket() {
        final User user = bookingFacade.getUserByEmail(TEST_USER_EMAIL);
        final Event event = bookingFacade.getEventById(1);
        final int place = 6;
        Assertions.assertTrue(Objects.nonNull(bookingFacade.bookTicket(user.getId(), event.getId(), place, Ticket.Category.PREMIUM)));
    }

    @Test
    @Order(3)
    public void Should_ThrowException_When_UserTryingToBookSamePlace() {
        final User user = bookingFacade.getUserByEmail(TEST_USER_EMAIL);
        final Event event = bookingFacade.getEventById(1);
        final int place = 6;
        Assertions.assertThrows(IllegalStateException.class, () -> bookingFacade.bookTicket(user.getId(), event.getId(), place, Ticket.Category.PREMIUM));
    }
}
