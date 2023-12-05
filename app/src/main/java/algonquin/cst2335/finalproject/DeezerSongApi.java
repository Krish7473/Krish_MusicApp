package algonquin.cst2335.finalproject;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import algonquin.cst2335.finalproject.KpSongs;

public class DeezerSongApi {

    private static final String TAG = DeezerSongApi.class.getSimpleName();
    private RequestQueue requestQueue;

    public DeezerSongApi(RequestQueue requestQueue) {
        this.requestQueue = requestQueue;
    }

    public interface DezApiResponseListener {
        void onSuccess(List<KpSongs> songs);

        void onFailure(String errorMessage);
    }

    public void searchSongsByArtist(String artistName, DezApiResponseListener listener) {
        String deezerAPIUrl = "https://api.deezer.com/search/artist/?q=" + artistName;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, deezerAPIUrl, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<KpSongs> songsList = parseDeezerResponse(response);
                        listener.onSuccess(songsList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onFailure("Error occurred: " + error.getMessage());
                    }
                });

        // Add the request to the RequestQueue
        requestQueue.add(jsonArrayRequest);
    }

    private List<KpSongs> parseDeezerResponse(JSONArray response) {
        List<KpSongs> songsList = new ArrayList<>();

        try {
            for (int i = 0; i < response.length(); i++) {
                JSONObject artistObject = response.getJSONObject(i);
                String tracklist = artistObject.optString("tracklist");

                // Retrieve individual songs using tracklist URL
                JsonArrayRequest songRequest = new JsonArrayRequest(Request.Method.GET, tracklist, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray songResponse) {
                                try {
                                    for (int j = 0; j < songResponse.length(); j++) {
                                        JSONObject songObject = songResponse.getJSONObject(j);
                                        String title = songObject.optString("title");
                                        String duration = songObject.optString("duration");
                                        String album = songObject.optString("album");
                                        String albumCover = songObject.optString("album_cover");

                                        KpSongs song = new KpSongs(title, duration, album, albumCover);
                                        songsList.add(song);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.e(TAG, "Error fetching song details: " + error.getMessage());
                            }
                        });

                // Add the song request to the RequestQueue
                requestQueue.add(songRequest);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return songsList;
    }
}
