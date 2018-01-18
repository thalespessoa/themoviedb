package com.pixformance.themovie.data.model;

import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by thalespessoa on 1/16/18.
 */
public class SearchSuggestion extends RealmObject {

    @PrimaryKey
    String text;
    Date date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
