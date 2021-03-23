package com.alexhinds.checkit;

import java.util.Date;

public class ListItem {
    // ATTRIBUTES
    private Date dateAdded;
    private String addedBy;
    private boolean markedDone;
    private String data;

    // CONSTRUCTORS
    ListItem() {}

    ListItem(String data, String addedBy, boolean markedDone) {
        this.dateAdded = new Date();
        this.addedBy = addedBy;
        this.markedDone = markedDone;
        this.data=data;
    }

    // GETTERS AND SETTERS

    public Date getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(Date dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getAddedBy() {
        return addedBy;
    }

    public void setAddedBy(String addedBy) {
        this.addedBy = addedBy;
    }

    public boolean isMarkedDone() {
        return markedDone;
    }

    public void setMarkedDone(boolean markedDone) {
        this.markedDone = markedDone;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
