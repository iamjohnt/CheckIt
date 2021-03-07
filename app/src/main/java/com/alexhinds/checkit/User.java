package com.alexhinds.checkit;

public class User {
    public String email;
    public String username;
    public String password;


    public User() { // necessary to have empty

    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }










}
