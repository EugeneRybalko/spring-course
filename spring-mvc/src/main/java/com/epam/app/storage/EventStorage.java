package com.epam.app.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epam.app.model.Event;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class EventStorage {

    private Map<Long, Event> map;
    private String filePath;

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<Long, Event> getMap() {
        return map;
    }

    private void init() throws IOException {
        Resource resource = new ClassPathResource(filePath);
        if (resource.isFile()) {
            String users = new String(Files.readAllBytes(resource.getFile().toPath()));
            this.map = new ObjectMapper().readValue(users, new TypeReference<Map<Long, Event>>() {
            });
        }
    }
}
