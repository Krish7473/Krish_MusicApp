package algonquin.cst2335.finalproject;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    private TextView titleTextView, durationTextView, albumTextView;
    private ImageView albumCoverImageView;
    private Button saveToFavoritesButton;

    // Additional variables for song details
    private String title, duration, album, albumCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Initialize views
        titleTextView = findViewById(R.id.titleTextView);
        durationTextView = findViewById(R.id.durationTextView);
        albumTextView = findViewById(R.id.albumTextView);
        albumCoverImageView = findViewById(R.id.albumCoverImageView);
        saveToFavoritesButton = findViewById(R.id.saveToFavoritesButton);

        // Retrieve song details from Intent or SharedPreferences
        retrieveSongDetails();

        // Display song details in views
        displaySongDetails();

        // Set OnClickListener for saveToFavoritesButton
        saveToFavoritesButton.setOnClickListener(v -> saveSongToFavorites());
    }

    private void retrieveSongDetails() {
        // Retrieve song details from Intent or SharedPreferences
        // Example: Retrieve data passed from the previous activity via Intent extras or SharedPreferences
        // You need to implement this logic based on how you pass data from the previous activity.
        // For instance, retrieving title, duration, album, albumCover from Intent extras or SharedPreferences.
        title = getIntent().getStringExtra("title");
        duration = getIntent().getStringExtra("duration");
        album = getIntent().getStringExtra("album");
        albumCover = getIntent().getStringExtra("albumCover");
    }

    private void displaySongDetails() {
        // Display song details in TextViews and ImageView
        titleTextView.setText(title);
        durationTextView.setText(duration);
        albumTextView.setText(album);
        // Set album cover image using Picasso or Glide library or by loading the image from URL
        // Example: Glide.with(this).load(albumCover).into(albumCoverImageView);
    }

    private void saveSongToFavorites() {
        // Save the current song to favorites using SharedPreferences or Room Database
        // Example: Store song details in SharedPreferences or Room Database as a favorite song
        // You'll need to implement this logic to save the song details.
        // For instance, storing the song title, duration, album, albumCover in SharedPreferences.
        SharedPreferences preferences = getSharedPreferences("Favorites", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("favoriteTitle", title);
        editor.putString("favoriteDuration", duration);
        editor.putString("favoriteAlbum", album);
        editor.putString("favoriteAlbumCover", albumCover);
        editor.apply();

        Toast.makeText(this, "Song saved to favorites", Toast.LENGTH_SHORT).show();
    }
}
