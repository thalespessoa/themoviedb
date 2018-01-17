package com.pixformance.themovie.data.model;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by thalespessoa on 1/16/18.
 */
public class SearchSuggestion extends RealmObject {

    @PrimaryKey
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
