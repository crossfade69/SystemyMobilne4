package com.example.todoapp;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();

    private final List<Task> tasks = new ArrayList<>();

    public static TaskStorage getInstance() {
        return taskStorage;
    }

    private TaskStorage () {
        for (int i = 0; i < 100; ++i) {
            Task task = new Task();
            task.setName(String.format("Task #%d", i));
            task.setDone(i % 3 == 0);
            if(i%3==0)
            {
                task.setCategory(Category.Studies);
            }
            else
            {
                task.setCategory(Category.Home);
            }
            tasks.add(task);
        }
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public Task getTask(UUID id) {
        for (Task task : tasks) {
            Log.d("UUID", String.format("%s, %s", id.toString(), task.getId().toString()));
            if (id.equals(task.getId())) {
                return task;
            }
        }

        return null;
    }
    public void addTask(Task task)
    {
        tasks.add(task);
    }
}
