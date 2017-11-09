package edu.uci.ics.fabflixmobile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MovieListActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ArrayList<String> titles = bundle.getStringArrayList("titles");
            if (titles.isEmpty()) {
                titles.add("No Result");
            }
            populateListView(titles);
        }
    }

    private void populateListView(ArrayList<String> titles) {
        // Build Adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.title_item, titles);

        ListView lvTitles = (ListView) findViewById(R.id.lvTitles);
        lvTitles.setAdapter(adapter);
    }
}
