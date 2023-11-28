package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private EditText artistEditText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        artistEditText = findViewById(R.id.artistEditText);
        searchButton = findViewById(R.id.searchButton);

        // Set onClick listener for search button
        searchButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String artistName = artistEditText.getText().toString().trim();
                if (!artistName.isEmpty()) {
                    // Perform Deezer API search
                    performDeezerAPISearch(artistName);
                } else {
                    // Handle empty input
                }
            }
        });
    }

    private void searchForArtist(String artistQuery) {
        String url = "https://api.deezer.com/search/artist/?q=" + artistQuery;

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Handle successful response - Parse JSON response for artist information
                        // Display artist information to the user
                        // Here you can process the response received from the API call
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            JSONArray data = jsonResponse.getJSONArray("data"); // Extract artist data array

                            // Iterate through artist data to get details
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject artist = data.getJSONObject(i);
                                String tracklistURL = artist.getString("tracklist");

                                // Make another API call using this tracklistURL to fetch songs
                                // Construct URL and use Volley similar to the previous API call
                                // This part would involve a similar Volley request to fetch the song details
                                // Handle the song details response as needed
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                // Here you can handle any errors that occur during the API call
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
