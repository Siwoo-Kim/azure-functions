package com.siwoo.azurefunction;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public enum ResourceManager {
    INSTANCE;

    private final ObjectMapper mapper = new ObjectMapper();
    private final ToDoRepository toDoRepository = new ToDoRepository();

    {
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public ObjectMapper getObjectMapper() {
        return mapper;
    }

    public ToDoRepository getToDoRepository() {
        return toDoRepository;
    }
}
