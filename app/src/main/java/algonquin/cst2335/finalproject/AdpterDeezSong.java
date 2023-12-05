package algonquin.cst2335.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdpterDeezSong extends RecyclerView.Adapter<AdpterDeezSong.SongViewHolder> {

    private List<KpSongs> songList;

    public AdpterDeezSong(List<KpSongs> songList) {
        this.songList = songList;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.song_item, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        KpSongs song = songList.get(position);
        holder.titleTextView.setText(song.getTitle());
        holder.durationTextView.setText(song.getDuration());
        holder.albumTextView.setText(song.getAlbumName());
        // Set album cover image using Picasso or Glide library or by loading the image from URL
        // Example: Glide.with(holder.itemView.getContext()).load(song.getAlbumCover()).into(holder.albumCoverImageView);
    }

    @Override
    public int getItemCount() {
        return songList.size();
    }

    static class SongViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, durationTextView, albumTextView;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            durationTextView = itemView.findViewById(R.id.durationTextView);
            albumTextView = itemView.findViewById(R.id.albumTextView);
        }
    }
}
