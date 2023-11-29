package algonquin.cst2335.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private EditText artistEditText;
    private Button searchButton;
    private RecyclerView recyclerView;
    private AdpterDeezSong songsAdapter;
    private List<KpSongs> songList;
    private DrezApiMangr apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        artistEditText = findViewById(R.id.artistEditText);
        searchButton = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.recyclerView);

        songList = new ArrayList<>();
        songsAdapter = new AdpterDeezSong(songList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songsAdapter);

        apiManager = new DrezApiMangr(this);

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

        SharedPreferences preferences = getSharedPreferences("SearchPrefs", MODE_PRIVATE);
        String previousSearch = preferences.getString("searchTerm", "");
        artistEditText.setText(previousSearch);
    }

    private void performDeezerAPISearch(String artistName) {
        apiManager.performDeezerAPISearch(artistName, new DrezApiMangr.DeezerApiCallback() {
            @Override
            public void onSuccess(List<KpSongs> fetchedSongList) {
                songList.clear();
                songList.addAll(fetchedSongList);
                songsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
            }
        });

        // Save the search term in SharedPreferences
        SharedPreferences.Editor editor = getSharedPreferences("SearchPrefs", MODE_PRIVATE).edit();
        editor.putString("searchTerm", artistName);
        editor.apply();
    }

    // onCreateOptionsMenu, onOptionsItemSelected, showInstructionsDialog methods remain unchanged
}
