package com.pixformance.themovie.data;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import com.pixformance.themovie.data.model.SearchSuggestion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by thalespessoa on 1/16/18.
 */

public class LocalStore {

    public LocalStore(Application application){
        Realm.init(application);

        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .schemaVersion(1)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(realmConfiguration);
    }

    public void search(final String term, final DataSource.OnFecthSuggestion onFecthSuggestion) {
        Realm.getDefaultInstance().executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmQuery query = realm.where(SearchSuggestion.class)
                        .contains("text", term);

                RealmResults<SearchSuggestion> realmObjects = query.findAll().sort("date", Sort.DESCENDING);

                List<String> result = new ArrayList<>();
                for(int i=0; i<realmObjects.size(); i++) {
                    SearchSuggestion searchSuggestion = realmObjects.get(i);
                    if(i<=10) {
                        result.add(searchSuggestion.getText());
                    } else {
                        searchSuggestion.deleteFromRealm();
                    }
                }
                final List<String> resultFinal = result;
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        if(onFecthSuggestion != null) {
                            onFecthSuggestion.onFetchSuggestionsSuccess(resultFinal);
                        }
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
                searchSuggestion.setDate(new Date());
                realm.copyToRealmOrUpdate(searchSuggestion);
            }
        });
    }
}
