package algonquin.cst2335.finalproject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.finalproject.databinding.ActivityPlaylistBinding;
import algonquin.cst2335.finalproject.databinding.SongPlaylistBinding;

/**
 * PlaylistActivity represents the main activity for the Deezer playlist functionality.
 * It displays a list of songs in a RecyclerView and allows users to search for songs and manage their playlist.
 */
public class PlaylistActivity extends AppCompatActivity {

    private RecyclerView.Adapter myAdapter;
    private RecyclerView recyclerView;
    private SongsViewModel songsViewModel;
    private ActivityPlaylistBinding binding;
    private SongsAdapter songsAdapter;
    private RequestQueue queue;
    private List<SongsEntity> songsList = new ArrayList<>();

    /**
     * Initializes the activity, sets up the UI components, and handles user interactions.
     * @param savedInstanceState A Bundle containing the saved state of the activity.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setTitle(R.string.playlistPage);

        setSupportActionBar(binding.toolbar);

        queue = Volley.newRequestQueue(this);

        songsAdapter = new SongsAdapter(songsList);
        songsViewModel = new ViewModelProvider(this).get(SongsViewModel.class);

        binding.searchPageButton.setOnClickListener(click -> {
            Intent intent = new Intent(this, DeezerMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        binding.playlistPageButton.setOnClickListener(click -> {
            startActivity(new Intent(this, PlaylistActivity.class));
        });

        SongsDatabase songsDatabase = Room.databaseBuilder(getApplicationContext(), SongsDatabase.class, "deezerDataBase").build();
        DeezerDataAccessObject dDao = songsDatabase.deezerDao();

        binding.searchButton.setOnClickListener(c -> {

            String searchedText = binding.searchTextPlaylist.getText().toString().trim();

            Executor thread1 = Executors.newSingleThreadExecutor();
            thread1.execute(() -> {
                List<SongsEntity> searchResults = dDao.searchSong(searchedText);
                runOnUiThread(() -> {
                    if (searchResults != null && !searchResults.isEmpty()) {
                        songsList.clear();
                        songsList.addAll(searchResults);
                        songsViewModel.songs.postValue(songsList);
                        myAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "Song not found: " + searchedText, Toast.LENGTH_SHORT).show();
                    }
                });
            });
        });


        if (songsList.isEmpty()) {
            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() -> {
                List<SongsEntity> allSongs = dDao.getAllSongs();
                Log.d("Database", "Number of songs: " + allSongs.size());
                runOnUiThread(() -> {
                    songsList.addAll(allSongs);
                    if (myAdapter == null) {
                        myAdapter = new SongsAdapter(songsList);
                        recyclerView.setAdapter(myAdapter);
                    } else {
                        myAdapter.notifyDataSetChanged();
                    }
                });
            });
        }

        recyclerView = binding.favSongs;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(songsAdapter);

    }

    /**
     * ViewHolder class for holding views of each item in the playlist RecyclerView.
     */
    public class SongsViewHolder extends RecyclerView.ViewHolder {

        TextView titleTextView;
        TextView artistTextView;
        TextView durationTextView;
        ImageView albumCoverSP;

        androidx.appcompat.widget.Toolbar songsPlaylist;

        /**
         * Constructor for the SongsViewHolder.
         * @param itemView The view representing an item in the RecyclerView.
         */
        SongsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.songName);
            artistTextView = itemView.findViewById(R.id.artistName);
            durationTextView = itemView.findViewById(R.id.duration);
            albumCoverSP = itemView.findViewById(R.id.albumCoverSP);
            songsPlaylist = itemView.findViewById(R.id.songToolsP);
        }

        /**
         * Binds data to the views in the ViewHolder.
         * @param songs The SongsEntity object containing song information.
         */
        public void bind(SongsEntity songs) {
            titleTextView.setText(songs.getTitle());
            artistTextView.setText(songs.getArtistName());
            durationTextView.setText(formatDuration(songs.getDuration()));

            String pathname = getFilesDir() + "/" + songs.getAlbumCover();
            File file = new File(pathname);

            if (file.exists()) {
                Bitmap albumCover = BitmapFactory.decodeFile(pathname);
                albumCoverSP.setImageBitmap(albumCover);
                albumCoverSP.setVisibility(View.VISIBLE);
            } else {
                ImageRequest imgReq = new ImageRequest(songs.getAlbumCover(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        albumCoverSP.setImageBitmap(bitmap);
                        albumCoverSP.setVisibility(View.VISIBLE);
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                    PlaylistActivity.this.openFileOutput(songs.getArtistName() + ".png", Activity.MODE_PRIVATE));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {
                    // Handle error in fetching the image
                    error.printStackTrace();
                });
                queue.add(imgReq);
            }
        }

        /**
         * Formats the duration of the song in minutes and seconds.
         * @param duration The duration of the song in seconds.
         * @return A formatted string representing the duration (MM:SS).
         */
        private String formatDuration(int duration) {
            int minutes = duration / 60;
            int seconds = duration % 60;
            return String.format("%02d:%02d", minutes, seconds);
        }
    }

    /**
     * Adapter class for the RecyclerView that displays the playlist.
     */
    class SongsAdapter extends RecyclerView.Adapter<SongsViewHolder> {

        private List<SongsEntity> songsList;

        /**
         * Constructor for the SongsAdapter.
         * @param songsList The list of SongsEntity objects to be displayed.
         */
        SongsAdapter(List<SongsEntity> songsList) {
            this.songsList = songsList;
        }

        /**
         * Creates a new ViewHolder instance when needed.
         * @param parent The parent ViewGroup.
         * @param viewType The type of the view.
         * @return A new SongsViewHolder instance.
         */
        @NonNull
        @Override
        public SongsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            SongPlaylistBinding playlistBinding = SongPlaylistBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
            return new SongsViewHolder(playlistBinding.getRoot());
        }

        /**
         * Binds data to the views in the ViewHolder.
         * @param holder The SongsViewHolder to bind data to.
         * @param position The position of the item in the RecyclerView.
         */
        @Override
        public void onBindViewHolder(@NonNull SongsViewHolder holder, int position) {
            SongsDatabase songsDatabase = Room.databaseBuilder(getApplicationContext(), SongsDatabase.class, "deezerDataBase").build();
            DeezerDataAccessObject dDao = songsDatabase.deezerDao();
            SongsEntity song = songsList.get(position);
            holder.bind(song);

            androidx.appcompat.widget.Toolbar toolbar = holder.songsPlaylist;
            toolbar.inflateMenu(R.menu.songsplaylist);

            toolbar.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.delete:
                        AlertDialog.Builder builder = new AlertDialog.Builder(PlaylistActivity.this);
                        builder.setMessage("Do you want to delete this song from your playlist?")
                                .setTitle("Delete")
                                .setNegativeButton("No", (dialog, which) -> {
                                })
                                .setPositiveButton("Yes", (dialog, which) -> {
                                    SongsEntity songToDelete = songsList.get(position);
                                    songsList.remove(songToDelete);

                                    if (songToDelete != null) {
                                        songsAdapter.notifyItemChanged(position);
                                        Executor thread1 = Executors.newSingleThreadExecutor();
                                        thread1.execute(() -> {
                                            dDao.deleteSongFromPlayList(songToDelete);
                                        });
                                        Log.d("SongToDelete", "Song to Delete: " + songToDelete.toString());

                                        Snackbar.make(findViewById(android.R.id.content), "Song Deleted from playlist", Snackbar.LENGTH_LONG)
                                                .setAction("Undo", (btn) -> {
                                                    Executor thread2 = Executors.newSingleThreadExecutor();
                                                    thread2.execute(() -> {
                                                        // undo the addition from the database
                                                        dDao.insertSong(songToDelete);
                                                    });

                                                    songsList.remove(songToDelete);
                                                    songsAdapter.notifyItemChanged(position);

                                                })
                                                .show();
                                    }
                                });
                        builder.create().show();
                        break;
                }

                return false;
            });

        }

        /**
         * Returns the total number of items in the dataset.
         * @return The total number of items.
         */
        @Override
        public int getItemCount() {
            return songsList.size();
        }
    }

    /**
     * Initializes the options menu.
     * @param menu The menu to be inflated.
     * @return true if the menu is successfully inflated.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deezer_menu, menu);
        return true;
    }

    }


