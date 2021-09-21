package dao;

import model.User;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import storage.UserStorage;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDaoTest {

    @Mock UserStorage userStorageMock;
    UserDao userDao;

    static User testUser;

    @BeforeAll
    static void beforeAll() {
        testUser = User.builder().id(1).name("Test Name").email("email@mail.com").build();
    }

    @BeforeEach
    void setUp() {
        userDao = new UserDao();
        userDao.setUserStorage(userStorageMock);
        when(userStorageMock.getMap()).thenReturn(Maps.newHashMap(testUser.getId(), testUser));
    }

    @Test
    void findUserById_UserIsRetrieved() {
        assertThat(userDao.findUserById(1)).isEqualTo(testUser);
    }

    @Test
    void findUserByEmail_UserIsRetrieved() {
        assertThat(userDao.findUserByEmail("email@mail.com")).isEqualTo(testUser);
    }

    @Test
    void findUserByName_UserIsRetrieved() {
        assertThat(userDao.findUserByName("Test Name")).isNotEmpty();
    }

    @Test
    void insertUser_UserIsInserted() {
        User expectedUser = User.builder().name("Test Name").email("test@mail.com").build();
        assertThat(userDao.insertUser(expectedUser)).isEqualTo(expectedUser);
    }

    @Test
    void updateUser_UserIsUpdated() {
        User expectedUser = User.builder().name("Test Name").email("test2@mail.com").build();
        userDao.insertUser(expectedUser);
        expectedUser.setName("Test");
        assertThat(userDao.updateUser(expectedUser)).isEqualTo(expectedUser);
    }

    @Test
    void deleteUser_UserIsDeleted() {
        User expectedUser = User.builder().name("Test Name").email("test3@mail.com").build();
        userDao.insertUser(expectedUser);
        assertTrue(userDao.deleteUser(expectedUser.getId()));
    }
}