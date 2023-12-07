package algonquin.cst2335.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import org.json.JSONArray;
import org.json.JSONObject;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import algonquin.cst2335.finalproject.databinding.AlbumViewHolderBinding;
import algonquin.cst2335.finalproject.databinding.DeezerMainActivityBinding;
import algonquin.cst2335.finalproject.R;

public class DeezerMainActivity extends AppCompatActivity {
    private RecyclerView.Adapter myAdapter;

    RequestQueue queue = null;
    ArrayList<SongsEntity> songsList = new ArrayList<>();

    ArrayList<AlbumEntity> albumsList = new ArrayList<>();

    AlbumsViewModel albumModel;

    DeezerMainActivityBinding binding;

    SongsViewModel songsModel;

    SharedPreferences sp;
    AlbumViewHolderBinding ab;
    SongsEntity song;

    protected Bitmap albumCover;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DeezerMainActivityBinding.inflate(getLayoutInflater());
        queue = Volley.newRequestQueue(this);
        setContentView(binding.getRoot());

        setTitle(R.string.app_name);


        // Set up the toolbar
        androidx.appcompat.widget.Toolbar toolBar = (binding.toolbar);
        setSupportActionBar(toolBar);

        // Set up the search button
        binding.searchPageButton.setOnClickListener(click -> {
            Intent intent = new Intent(this, DeezerMainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        });

        // Display a welcome message
        AlertDialog.Builder builder = new AlertDialog.Builder(DeezerMainActivity.this);
        builder.setMessage("Welcome to your personalized Deezer experience! Follow these steps to curate your perfect playlists:\n1. Search for your beloved artists using the Search Icon and explore their albums.\n2. Simply select an album to view and save all the tracks you love.\n3. Click the three-dotted icon to either preview or add your chosen songs.\n4. Tap the playlist icon to access all your favorite tunes conveniently.\n5. Delete any song from your playlist effortlessly with just a click.\n6. And most importantly, immerse yourself in the world of Deezer. Enjoy the music!"
                ).setTitle("Welcome To Deezer")
                .setPositiveButton("Okay", (dialog, which) -> {
                    dialog.dismiss();
                });

        // Set up the playlist button
        binding.playlistPageButton.setOnClickListener(click -> {
            startActivity(new Intent(this, PlaylistActivity.class));
        });

        // Set up the RecyclerView for displaying albums
        binding.deezerAlbums.setLayoutManager(new LinearLayoutManager(this));
        albumModel = new ViewModelProvider(this).get(AlbumsViewModel.class);

        // Set up the search button functionality
        binding.searchButton.setOnClickListener(c -> {
            String searchedText = binding.searchText.getText().toString().trim();

            sp = getSharedPreferences("myData", Context.MODE_PRIVATE);
            sp.getString("artistsSearched", searchedText);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("artistsSearched", searchedText);
            editor.apply();

            String stringURL = null;
            try {
                stringURL = "https://api.deezer.com/search/album/?q=" +
                        URLEncoder.encode(searchedText, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            }

            // Make a GET request to the Deezer API
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, stringURL, null,
                    (response) -> {
                        try {
                            // Get the array of albums from the response
                            JSONArray albumArray = response.getJSONArray("data");

                            // Clear the existing albumsList before adding new data
                            albumsList.clear();

                            for (int i = 0; i < albumArray.length(); i++) {
                                JSONObject album0 = albumArray.getJSONObject(i);
                                long albumId = album0.getLong("id");
                                String albumName = album0.getString("title");
                                String albumCoverUrl = album0.getString("cover_xl");

                                // Get the artist information
                                JSONObject artist = album0.getJSONObject("artist");
                                String artistName = artist.getString("name");

                                AlbumEntity album = new AlbumEntity(albumId, albumName, artistName, albumCoverUrl);
                                albumsList.add(album);
                            }

                            // Notify the adapter that the data set has changed
                            myAdapter.notifyDataSetChanged();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    },
                    error -> {
                        // Handle error
                        error.printStackTrace();
                    });
            queue.add(request);
        });

        // Set up observer for selected albums
        albumModel.selectedAlbums.observe(this, album -> {
            if (album != null) {
                try {
                    // Construct the URL for the Deezer API to get tracks of the selected album
                    String tracksURL = "https://api.deezer.com/album/" + album.getAlbumId() + "/tracks";

                    // Make a GET request to the Deezer API to get tracks of the selected album
                    JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, tracksURL, null,
                            (response) -> {
                                try {
                                    // Get the array of albums from the response
                                    JSONArray songsArray = response.getJSONArray("data");

                                    // Clear the existing songsList before adding new data
                                    for (int i = 0; i < songsArray.length(); i++) {
                                        JSONObject trackObject = songsArray.getJSONObject(i);
                                        long trackId = trackObject.getLong("id");
                                        String trackTitle = trackObject.getString("title");
                                        String artistName = trackObject.getJSONObject("artist").getString("name");
                                        int duration = trackObject.getInt("duration");

                                        SongsEntity track = new SongsEntity(trackId, trackTitle, duration, album.getTitle(), album.getCoverUrl(), artistName);
                                        songsList.add(track);
                                    }

                                    // Notify the adapter that the data set has changed
                                    myAdapter.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                AlbumDetailFragment albumDetailFragment = new AlbumDetailFragment(songsList, album, queue);
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();

                                transaction.addToBackStack("");
                                transaction.replace(R.id.albumFragment, albumDetailFragment);
                                transaction.commit();
                            },
                            error -> {
                                // Handle error in fetching tracks
                                error.printStackTrace();
                            });
                    queue.add(request); // Add the tracks request

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Set up the RecyclerView adapter for albums
        binding.deezerAlbums.setAdapter(myAdapter = new RecyclerView.Adapter<MyAlbumHolder>() {
            @NonNull
            @Override
            public MyAlbumHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                AlbumViewHolderBinding ab = AlbumViewHolderBinding.inflate(getLayoutInflater(), parent, false);
                return new MyAlbumHolder(ab.getRoot());
            }

            @Override
            public void onBindViewHolder(@NonNull MyAlbumHolder holder, int position) {
                AlbumEntity deezerAlbum = albumsList.get(position);
                holder.bind(deezerAlbum);
            }

            @Override
            public int getItemCount() {
                return albumsList.size();
            }
        });
    }

    // ViewHolder for displaying albums in RecyclerView
    class MyAlbumHolder extends RecyclerView.ViewHolder {
        TextView albumName;
        TextView artistName;
        ImageView imageView;
        Toolbar albumMenu;

        public MyAlbumHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(c -> {
                int position = getAdapterPosition();
                AlbumEntity selected = albumsList.get(position);
                albumModel.selectedAlbums.postValue(selected);
            });

            albumName = itemView.findViewById(R.id.albumName);
            artistName = itemView.findViewById(R.id.artistName);
            imageView = itemView.findViewById(R.id.albumCover);
        }

        // Bind album data to the ViewHolder
        public void bind(AlbumEntity deezerAlbum) {
            albumName.setText(deezerAlbum.getTitle());
            artistName.setText(deezerAlbum.getArtistName());

            String pathname = getFilesDir() + "/" + deezerAlbum.getCoverUrl();
            File file = new File(pathname);

            if (file.exists()) {
                albumCover = BitmapFactory.decodeFile(pathname);
                imageView.setImageBitmap(albumCover);
                imageView.setVisibility(View.VISIBLE);
            } else {
                ImageRequest imgReq = new ImageRequest(deezerAlbum.getCoverUrl(), new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                        try {
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100,
                                    DeezerMainActivity.this.openFileOutput(deezerAlbum.getArtistName() + ".png", Activity.MODE_PRIVATE));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, 1024, 1024, ImageView.ScaleType.CENTER, null, error -> {
                });
                queue.add(imgReq);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.deezer_menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sunrise:
//                startActivity(new Intent(this, SunriseMain.class));
                break;
            case R.id.dictionary:
//                startActivity(new Intent(this, DictionaryActivity.class));
                break;
            case R.id.recipe:
//                startActivity(new Intent(this, RecipeMain.class));
                break;
            case R.id.info:
                AlertDialog.Builder builder = new AlertDialog.Builder(DeezerMainActivity.this);
                builder.setMessage("Welcome to your personalized Deezer experience! Follow these steps to curate your perfect playlists:\n1. Search for your beloved artists using the Search Icon and explore their albums.\n2. Simply select an album to view and save all the tracks you love.\n3. Click the three-dotted icon to either preview or add your chosen songs.\n4. Tap the playlist icon to access all your favorite tunes conveniently.\n5. Delete any song from your playlist effortlessly with just a click.\n6. And most importantly, immerse yourself in the world of Deezer. Enjoy the music!"
                        )
                        .setTitle("Welcome To Deezer")
                        .setPositiveButton("Okay", (dialog, which) -> {
                            dialog.dismiss();
                        }).show();
        }
        return true;
    }
}
