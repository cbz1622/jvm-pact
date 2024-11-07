package io.pact.models;

public class User {
    public User(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private int id;
    private String name;

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "'}";
    }
}
