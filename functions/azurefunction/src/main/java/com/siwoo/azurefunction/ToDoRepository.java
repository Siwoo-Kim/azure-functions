package com.siwoo.azurefunction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.google.common.base.Preconditions.checkNotNull;

public class ToDoRepository {

    private final Map<String, ToDo> db = new ConcurrentHashMap<>();

    public void save(ToDo toDo) {
        checkNotNull(toDo);
        db.put(toDo.getId(), toDo);
    }

    public List<ToDo> getAll() {
        return new ArrayList<>(db.values());
    }

    public ToDo getById(String id) {
        checkNotNull(id);
        return db.get(id);
    }
}
