package service;

import dao.UserDao;
import model.User;
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
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock UserDao userDaoMock;
    UserService userService;

    static User testUser;

    @BeforeAll
    static void beforeAll() {
        testUser = User.builder().id(1).name("Test Name").email("test@mail.com").build();
    }

    @BeforeEach
    void setUp() {
        userService = new UserService();
        userService.setUserDao(userDaoMock);
    }

    @Test
    void retrieveUserById_UserIsRetrieved() {
        when(userDaoMock.findUserById(anyLong())).thenReturn(testUser);
        assertThat(userService.retrieveUserById(1)).isEqualTo(testUser);
    }

    @Test
    void retrieveUserByEmail_UserIsRetrieved() {
        when(userDaoMock.findUserByEmail(anyString())).thenReturn(testUser);
        assertThat(userService.retrieveUserByEmail("test@mail.com")).isEqualTo(testUser);
    }

    @Test
    void retrieveUserByName_UserIsRetrieved() {
        when(userDaoMock.findUserByName(anyString())).thenReturn(Lists.newArrayList(testUser));
        assertThat(userService.retrieveUserByName("Test Name")).isNotEmpty();
    }

    @Test
    void addUser_UserIsAdded() {
        when(userDaoMock.insertUser(any(User.class))).thenReturn(testUser);
        assertThat(userService.addUser(testUser)).isEqualTo(testUser);
    }

    @Test
    void updateUser_UserIsUpdated() {
        when(userDaoMock.updateUser(any(User.class))).thenReturn(testUser);
        assertThat(userService.updateUser(testUser)).isEqualTo(testUser);
    }

    @Test
    void removeUser_UserIsRemoved() {
        when(userDaoMock.deleteUser(anyLong())).thenReturn(true);
        assertTrue(userService.removeUser(1));
    }
}