package algonquin.cst2335.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private EditText artistEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private AdpterDeezSong songsAdapter;
    private List<KpSongs> songList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistEditText = findViewById(R.id.artistEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);

        // Set up RecyclerView and adapter
        songList = new ArrayList<>();
        songsAdapter = new AdpterDeezSong(songList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songsAdapter);

        // Set OnClickListener for Button
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String artistName = artistEditText.getText().toString().trim();
                if (!artistName.isEmpty()) {
                    performDeezerAPISearch(artistName);
                } else {
                    Toast.makeText(MainActivity.this, "Please enter an artist name", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Check if SharedPreferences contains a previous search term and display it in the EditText
        SharedPreferences preferences = getSharedPreferences("SearchPrefs", MODE_PRIVATE);
        String previousSearch = preferences.getString("searchTerm", "");
        artistEditText.setText(previousSearch);
    }

    private void performDeezerAPISearch(String artistName) {
        String deezerAPIUrl = "https://api.deezer.com/search/artist/?q=" + artistName;

        // Using Volley for API call
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, deezerAPIUrl, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray results = response.getJSONArray("data");
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject anAlbum = results.getJSONObject(i);
                                String tracklist = anAlbum.getString("tracklist");

                                // Nested Volley request for individual song details
                                JsonObjectRequest songLookup = new JsonObjectRequest(Request.Method.GET, tracklist, null,
                                        new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject songRequest) {
                                                try {
                                                    JSONArray albumData = songRequest.getJSONArray("data");
                                                    for (int k = 0; k < albumData.length(); k++) {
                                                        JSONObject thisAlbum = albumData.getJSONObject(k);
                                                        String title = thisAlbum.getString("title");
                                                        int duration = thisAlbum.getInt("duration");
                                                        String album = anAlbum.getString("picture_small");
                                                        String name = anAlbum.getString("name");

                                                        // Create KpSongs object and add to songList
                                                        KpSongs song = new KpSongs(title, String.valueOf(duration), name);
                                                        songList.add(song);
                                                        songsAdapter.notifyDataSetChanged(); // Notify adapter
                                                    }
                                                } catch (JSONException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        },
                                        new Response.ErrorListener() {
                                            @Override
                                            public void onErrorResponse(VolleyError error) {
                                                // Handle error while fetching song details
                                            }
                                        });

                                queue.add(songLookup);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Handle error while making API call
                        Toast.makeText(MainActivity.this, "Error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        queue.add(jsonObjectRequest);

        // Storing user's search term in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("SearchPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("searchTerm", artistName);
        editor.apply();

        Toast.makeText(MainActivity.this, "Searching for artist: " + artistName, Toast.LENGTH_SHORT).show();
    }

    // Other methods
}