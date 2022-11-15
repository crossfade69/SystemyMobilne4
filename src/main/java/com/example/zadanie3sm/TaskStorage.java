package com.example.zadanie3sm;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TaskStorage {
    private static final TaskStorage taskStorage = new TaskStorage();

    private final List<Task> tasks;

    private TaskStorage(){
        tasks = new ArrayList<>();
        for(int i = 1; i <=130; i++){
            Task task = new Task();
            task.setName("Misja " + i);
            task.setDone(i % 3 == 0);

            if(i%3==0){
                task.setCategory(Category.STUDIA);
            } else{
                task.setCategory(Category.DOM);
            }
            tasks.add(task);
        }
    }

    public static TaskStorage getInstance(){
        return taskStorage;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void addTask(Task task){
        tasks.add(task);
    }

    public Task getTask(UUID id) {
        for(int i = 0; i < tasks.size(); i++){
            if(tasks.get(i).getId().equals(id)) {
                return tasks.get(i);
            }
        }
        return null;
    }
}
