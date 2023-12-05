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
        searchButton.setOnClickListener(v -> {
            String artistName = artistEditText.getText().toString().trim();
            if (!artistName.isEmpty()) {
                performDeezerAPISearch(artistName);
            } else {
                Toast.makeText(MainActivity.this, "Please enter an artist name", Toast.LENGTH_SHORT).show();
            }
        });

        // Check if SharedPreferences contains a previous search term and display it in the EditText
        SharedPreferences preferences = getSharedPreferences("SearchPrefs", MODE_PRIVATE);
        String previousSearch = preferences.getString("searchTerm", "");
        artistEditText.setText(previousSearch);
    }

    // Inside the performDeezerAPISearch method
    private void performDeezerAPISearch(String artistName) {
        // Deezer API URL for artist search
        String deezerAPIUrl = "https://api.deezer.com/search/artist/?q=" + artistName;

        // RequestQueue for API calls
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, deezerAPIUrl, null,
                response -> {
                    try {
                        JSONArray results = response.getJSONArray("data");
                        for (int i = 0; i < results.length(); i++) {
                            JSONObject anArtist = results.getJSONObject(i);
                            String tracklist = anArtist.getString("tracklist");

                            // Call method to fetch tracklist and songs
                            fetchTracklistAndSongs(tracklist);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error occurred: " + error.getMessage(), Toast.LENGTH_SHORT).show()
        );

        // Adding the API request to the queue
        queue.add(jsonObjectRequest);

        // Storing user's search term in SharedPreferences
        SharedPreferences preferences = getSharedPreferences("SearchPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("searchTerm", artistName);
        editor.apply();

        Toast.makeText(MainActivity.this, "Searching for artist: " + artistName, Toast.LENGTH_SHORT).show();
    }

    private void fetchTracklistAndSongs(String tracklistUrl) {
        RequestQueue queue = Volley.newRequestQueue(this);
        JsonObjectRequest songLookup = new JsonObjectRequest(Request.Method.GET, tracklistUrl, null,
                response -> {
                    try {
                        JSONArray albumData = response.getJSONArray("data");

                        // Create a new list to store songs for each album
                        List<KpSongs> songsForAlbum = new ArrayList<>();

                        for (int k = 0; k < albumData.length(); k++) {
                            JSONObject thisSong = albumData.getJSONObject(k);
                            String title = thisSong.getString("title");
                            int duration = thisSong.getInt("duration");
                            String album = thisSong.getString("album");
                            String albumCover = thisSong.getString("album_cover");

                            // Create KpSongs object and add to the album's song list
                            KpSongs song = new KpSongs(title, String.valueOf(duration), album, albumCover);
                            songsForAlbum.add(song);
                        }

                        // Add all songs for the current album to the main songList
                        songList.addAll(songsForAlbum);
                        songsAdapter.notifyDataSetChanged(); // Notify adapter
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(MainActivity.this, "Error fetching song details", Toast.LENGTH_SHORT).show()
        );

        // Adding the request to the queue
        queue.add(songLookup);
    }
}
