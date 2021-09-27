package dao;

import model.User;
import storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserDao {
    
    private UserStorage userStorage;

    public void setUserStorage(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User findUserById(long userId) {
        return userStorage.getMap().get(userId);
    }

    public User findUserByEmail(String email) {
        return userStorage.getMap().values().stream().filter(user -> email.equals(user.getEmail())).findFirst()
                .orElseThrow(() -> new IllegalStateException("The are no user with email: " + email));
    }

    public List<User> findUserByName(String name) {
        return userStorage.getMap().values().stream().filter(user -> name.equals(user.getName())).collect(Collectors.toList());
    }

    public User insertUser(User user) {
        Optional<User> maxUser = userStorage.getMap().values().stream().max(Comparator.comparingLong(User::getId));
        long maxId = 1;
        if (maxUser.isPresent()) {
            maxId = maxUser.get().getId() + 1;
        }
        user.setId(maxId);
        userStorage.getMap().put(maxId, user);
        return userStorage.getMap().get(maxId);
    }

    public User updateUser(User user) {
        if (userStorage.getMap().containsKey(user.getId())) {
            userStorage.getMap().put(user.getId(), user);
            return user;
        }
        throw new IllegalStateException("There are no event with id: " + user.getId());
    }

    public boolean deleteUser(long userId) {
        return Objects.nonNull(userStorage.getMap().remove(userId));
    }
}
