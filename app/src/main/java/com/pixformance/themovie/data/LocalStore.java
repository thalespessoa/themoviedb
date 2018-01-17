package com.pixformance.themovie.data;

import android.os.Handler;
import android.os.Looper;

import com.pixformance.themovie.data.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class LocalStore {


    public void search(final String term, final DataSource.OnFecthSuggestion onFecthSuggestion) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery query = realm.where(SearchSuggestion.class)
                        .contains("text", term);

                RealmResults<SearchSuggestion> realmObjects = query.findAll();

                List<String> result = new ArrayList<>();
                for(SearchSuggestion searchSuggestion:realmObjects) {
                    result.add(searchSuggestion.getText());
                }
                final List<String> resultFinal = result;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        onFecthSuggestion.onFetchSuccess(resultFinal);
                    }
                });
            }
        });
    }

    public void save(final String suggestion) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                SearchSuggestion searchSuggestion = new SearchSuggestion();
                searchSuggestion.setText(suggestion);
                realm.copyToRealmOrUpdate(searchSuggestion);
            }
        });
    }
}
