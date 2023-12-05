package algonquin.cst2335.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavoriteSongActivity extends AppCompatActivity {

    private ListView favoritesListView;
    private List<String> favoriteSongs;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        favoritesListView = findViewById(R.id.favoritesListView);
        favoriteSongs = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, favoriteSongs);
        favoritesListView.setAdapter(adapter);

        // Load favorite songs on activity creation
        loadFavoriteSongs();

        // Set OnItemClickListener for favoritesListView to view details or delete a song
        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSong = favoriteSongs.get(position);
                // Implement logic to view details or delete the selected song
                // Example: Start DetailsActivity to view details of the selected song
                // Intent intent = new Intent(FavoritesActivity.this, DetailsActivity.class);
                // intent.putExtra("selectedSong", selectedSong);
                // startActivity(intent);
            }
        });

        // Set OnItemLongClickListener for favoritesListView to delete a song from favorites
        favoritesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedSong = favoriteSongs.get(position);
                // Implement logic to delete the selected song from favorites
                deleteFavoriteSong(selectedSong);
                return true;
            }
        });
    }

    private void saveFavoriteSong(String selectedSong) {
        SharedPreferences preferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // Retrieve existing favorites
        String favoritesJson = preferences.getString("favoriteSongs", "[]");
        try {
            JSONArray favoritesArray = new JSONArray(favoritesJson);

            // Add the selected song to favorites
            favoritesArray.put(selectedSong);

            // Save updated favorites list
            editor.putString("favoriteSongs", favoritesArray.toString());
            editor.apply();
            Toast.makeText(this, "Song saved to favorites", Toast.LENGTH_SHORT).show();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void loadFavoriteSongs() {
        SharedPreferences preferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        String favoritesJson = preferences.getString("favoriteSongs", "[]");
        try {
            JSONArray favoritesArray = new JSONArray(favoritesJson);

            favoriteSongs.clear();
            for (int i = 0; i < favoritesArray.length(); i++) {
                String songDetails = favoritesArray.getString(i);
                favoriteSongs.add(songDetails);
            }

            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void deleteFavoriteSong(String selectedSong) {
        SharedPreferences preferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        String favoritesJson = preferences.getString("favoriteSongs", "[]");
        try {
            JSONArray favoritesArray = new JSONArray(favoritesJson);

            // Remove the selected song from favorites
            favoritesArray.remove(selectedSong);

            // Save updated favorites list
            editor.putString("favoriteSongs", favoritesArray.toString());
            editor.apply();

            Toast.makeText(this, "Song removed from favorites", Toast.LENGTH_SHORT).show();
            loadFavoriteSongs();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}