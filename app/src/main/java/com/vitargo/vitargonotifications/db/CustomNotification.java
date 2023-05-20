package com.vitargo.vitargonotifications.db;

import androidx.annotation.NonNull;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class CustomNotification implements Serializable {

    private int id;
    private String text;
    private String title;
    private String date;

    public CustomNotification() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @NonNull
    @NotNull
    @Override
    public String toString() {
        return "CustomNotification = {Title = " + this.getTitle()
                + ", Text = " + this.getText()
                +", date = " + this.date;
    }
}
