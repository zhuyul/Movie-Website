package edu.uci.ics.fabflixmobile;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserMenuActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_menu);

        final EditText etSearchBox = (EditText) findViewById(R.id.etSearchBox);
        final Button bSearch = (Button) findViewById(R.id.bSearch);

        bSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String searchText = etSearchBox.getText().toString();

                if (searchText.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(UserMenuActivity.this);
                    builder.setMessage("Input is empty")
                            .setNegativeButton("Retry", null)
                            .create()
                            .show();
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        try {
                            JSONObject jsonResponse = new JSONObject(s);
                            JSONArray titleArray = jsonResponse.getJSONArray("titles");

                            ArrayList<String> titles = new ArrayList<>();

                            for (int i = 0; i < titleArray.length(); i++) {
                                titles.add(titleArray.getString(i));
                            }

                            Intent intent = new Intent(UserMenuActivity.this, MovieListActivity.class);
                            intent.putExtra("titles", titles);

                            UserMenuActivity.this.startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                SearchRequest searchRequest = new SearchRequest(searchText, responseListener);
                RequestQueue queue = Volley.newRequestQueue(UserMenuActivity.this);
                queue.add(searchRequest);
            }
        });

    }
}
