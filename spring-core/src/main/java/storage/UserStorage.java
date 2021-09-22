package storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class UserStorage {

    private String filePath;
    private Map<Long, User> map;

    private void init() throws IOException {
        Resource resource = new ClassPathResource(filePath);
        if (resource.isFile()) {
            String users = new String(Files.readAllBytes(resource.getFile().toPath()));
            this.map = new ObjectMapper().readValue(users, new TypeReference<Map<Long, User>>() {});
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<Long, User> getMap() {
        return map;
    }
}
