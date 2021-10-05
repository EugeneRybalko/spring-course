package com.epam.app.storage;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.epam.app.model.Ticket;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class TicketStorage {

    private String filePath;
    private Map<Long, Ticket> map;

    private void init() throws IOException {
        final Resource resource = new ClassPathResource(filePath);
        if (resource.isFile()) {
            String users = new String(Files.readAllBytes(resource.getFile().toPath()));
            this.map = new ObjectMapper().readValue(users, new TypeReference<Map<Long, Ticket>>() {});
        }
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Map<Long, Ticket> getMap() {
        return map;
    }
}
