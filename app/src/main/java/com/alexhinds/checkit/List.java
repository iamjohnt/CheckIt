package com.alexhinds.checkit;

public class List {


    // ATTRIBUTES
    private String id;
    private String category;
    private String dateCreated;
    private boolean hasDeadline;
    private String deadline;
    private boolean isShareable;
    private String shareWith;
    private String owner;
    private java.util.List<ListItem> items;

    // DEFAULT CONSTRUCTOR: don't remove!
    public List() {
    }

    // CONSTRUCTOR
    public List(String listId, String category, String dateCreated, boolean hasDeadline, String deadline, boolean isShareable, String shareWith, String owner) {
        this.id = listId;
        this.category = category;
        this.dateCreated = dateCreated;
        this.hasDeadline = hasDeadline;
        this.deadline = deadline;
        this.isShareable = isShareable;
        this.shareWith = shareWith;
        this.owner = owner;
    }


    // GETTERS & SETTERS
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public java.util.List<ListItem> getItems() {
        return items;
    }

    public void setItems(java.util.List<ListItem> items) {
        this.items = items;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    public boolean isHasDeadline() {
        return hasDeadline;
    }

    public void setHasDeadline(boolean hasDeadline) {
        this.hasDeadline = hasDeadline;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public boolean isShareable() {
        return isShareable;
    }

    public void setShareable(boolean shareable) {
        isShareable = shareable;
    }

    public String getShareWith() {
        return shareWith;
    }

    public void setShareWith(String shareWith) {
        this.shareWith = shareWith;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}