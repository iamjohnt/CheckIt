package com.se.checkit;

import java.util.Map;

public class User {
    public String id; // value is user's generated UID from Firebase auth;
    public String email;
    public String username;
    public String password;
    public Map<String, Boolean> ownedLists;


    public User() { // necessary to have empty

    }

    public User(String uid, String email, String username, String password) {
        this.id = uid;
        this.email = email;
        this.username = username;
        this.password = password;
        this.ownedLists = null;

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
