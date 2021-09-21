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
class BookingFacadeIntegrationTest {

    static final String TEST_USER_EMAIL = "yemail@gmail.com";

    @Autowired
    BookingFacade bookingFacade;

    @Test
    @Order(1)
    void Should_Pass_When_UserIsCreated() {
        User user = User.builder().name("Yevhenii").email(TEST_USER_EMAIL).build();
        bookingFacade.createUser(user);
        Assertions.assertNotEquals(0, user.getId());
    }

    @Test
    @Order(2)
    void Should_Pass_When_UserBookedTicket() {
        User user = bookingFacade.getUserByEmail(TEST_USER_EMAIL);
        Event event = bookingFacade.getEventById(1);
        int place = 6;
        Assertions.assertTrue(Objects.nonNull(bookingFacade.bookTicket(user.getId(), event.getId(), place, Ticket.Category.PREMIUM)));
    }

    @Test
    @Order(3)
    void Should_ThrowException_When_UserTryingToBookSamePlace() {
        User user = bookingFacade.getUserByEmail(TEST_USER_EMAIL);
        Event event = bookingFacade.getEventById(1);
        int place = 6;
        long userId = user.getId();
        long eventId = event.getId();
        Assertions.assertThrows(IllegalStateException.class, () -> bookingFacade.bookTicket(userId, eventId, place, Ticket.Category.PREMIUM));
    }
}
