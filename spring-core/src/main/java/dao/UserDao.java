package dao;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
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

public final class UserDao {

    private String filePath;
    private Map<Long, User> userStorage;


    private void init() throws IOException {
        Resource resource = new ClassPathResource(filePath);
        if (resource.isFile()) {
            String users = new String(Files.readAllBytes(resource.getFile().toPath()));
            this.userStorage = new ObjectMapper().readValue(users, new TypeReference<Map<Long, User>>() {});
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public User findUserById(long userId) {
        return userStorage.get(userId);
    }

    public User findUserByEmail(String email) {
        return userStorage.values().stream().filter(user -> email.equals(user.getEmail())).findFirst()
                .orElseThrow(() -> new IllegalStateException("The are no user with email: " + email));
    }

    public List<User> findUserByName(String name) {
        return userStorage.values().stream().filter(user -> name.equals(user.getName())).collect(Collectors.toList());
    }

    public User insertUser(User user) {
        Optional<User> maxUser = userStorage.values().stream().max(Comparator.comparingLong(User::getId));
        long maxId = 1;
        if (maxUser.isPresent()) {
            maxId = maxUser.get().getId() + 1;
        }
        user.setId(maxId);
        userStorage.put(maxId, user);
        return userStorage.get(maxId);
    }

    public User updateUser(User user) {
        if (userStorage.containsKey(user.getId())) {
            userStorage.put(user.getId(), user);
            return user;
        } else {
            throw new IllegalStateException("There are no event with id: " + user.getId());
        }
    }

    public boolean deleteUser(long userId) {
        return Objects.nonNull(userStorage.remove(userId));
    }
}
