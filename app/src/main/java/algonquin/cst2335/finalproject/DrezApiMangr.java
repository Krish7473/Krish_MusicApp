package algonquin.cst2335.finalproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

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

public class DrezApiMangr {

    private final Context context;
    private final RequestQueue requestQueue;

    public DrezApiMangr(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void performDeezerAPISearch(String artistName, final DeezerApiCallback callback) {
        String url = "https://api.deezer.com/search/artist/?q=" + artistName;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray data = response.getJSONArray("data");
                            List<KpSongs>songList = new ArrayList<>();

                            // Parse the JSON response and create DeezerSong objects
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject artist = data.getJSONObject(i);
                                String tracklistURL = artist.getString("tracklist");

                                // Fetch songs details based on tracklistURL
                                // Implement the method to retrieve songs details from the tracklist URL

                                // For now, adding a placeholder song to the list
                                KpSongs song = new KpSongs("Song Title", "Duration", "Album");
                                songList.add(song);
                            }

                            // Callback to MainActivity with the list of songs
                            callback.onSuccess(songList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                            callback.onError("Error parsing API response");
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        callback.onError("API request failed");
                    }
                });

        requestQueue.add(jsonObjectRequest);
    }

    // Implement other Deezer-related functionalities here
    // ...

    public interface DeezerApiCallback {
        void onSuccess(List<KpSongs> songList);

        void onError(String errorMessage);
    }
}
