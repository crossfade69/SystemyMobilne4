package com.example.todoapp;
import java.util.Date;
import java.util.UUID;

public class Task {
    private UUID id = UUID.randomUUID();
    private Date date = new Date();
    private boolean done = false;
    private Category category=Category.Home;
    private String name;

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public Date getDate() {
        return date;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public void setDate(Date date) {
        this.date=date;
    }
}
