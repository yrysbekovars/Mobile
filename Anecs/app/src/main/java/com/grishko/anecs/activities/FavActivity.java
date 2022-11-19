package com.grishko.anecs.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.grishko.anecs.JokeApp;
import com.grishko.anecs.R;
import com.grishko.anecs.anecdotes.AnecController;
import com.grishko.anecs.database.JokeData;

import java.util.Objects;

import io.realm.Realm;
import io.realm.RealmResults;

public class FavActivity extends AppCompatActivity {
    private TextView textView;
    private int id = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        textView = findViewById(R.id.textViewAnecFav);

        Button nextButton = findViewById(R.id.btnNextFav);
        nextButton.setOnClickListener(view -> nextAnec());

        Button backButton = findViewById(R.id.btnBackFav);
        backButton.setOnClickListener(view -> prevAnec());

        Button deleteButton = findViewById(R.id.btnDeleteFav);
        deleteButton.setOnClickListener(view -> delete());

        updateAnec();
    }

    public void updateAnec() {
        Realm realm = ((JokeApp)getApplication()).getRealmInstance();

        realm.beginTransaction();
        boolean isNotEmpty = realm.where(JokeData.class).count() > 0;
        realm.commitTransaction();

        if (isNotEmpty)
            nextAnec();
        else
            showJoke(getString(R.string.no_fav));
    }

    public void prevAnec()
    {
        Realm realm = ((JokeApp)getApplication()).getRealmInstance();

        realm.beginTransaction();
        RealmResults<JokeData> results = realm.where(JokeData.class)
                .lessThan("id", id).findAll();
        realm.commitTransaction();

        if (results.isEmpty())
            return;

        JokeData joke = results.last();
        if (joke != null) {
            id = joke.getId();
            showJoke(joke.getText());
        }
    }

    public void nextAnec()
    {
        Realm realm = ((JokeApp)getApplication()).getRealmInstance();
        realm.beginTransaction();
        JokeData joke = realm.where(JokeData.class)
                .greaterThan("id", id).findFirst();
        realm.commitTransaction();

        if (joke != null) {
            id = joke.getId();
            showJoke(joke.getText());
        }
    }

    protected void showJoke(String text) { textView.setText(text); }

    protected void delete() {
        Realm realm = ((JokeApp) getApplication()).getRealmInstance();

        realm.beginTransaction();
        RealmResults<JokeData> result = realm.where(JokeData.class)
                .equalTo("id", id)
                .findAll();
        if (result.isEmpty())
            Log.e("realm", "Warning: id " + id + " not found in database.");
        else
            Objects.requireNonNull(result.first()).deleteFromRealm();
        realm.commitTransaction();

        updateAnec();
    }
}