package algonquin.cst2335.finalproject;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdpterforFavorites extends RecyclerView.Adapter<AdpterforFavorites.FavoriteSongViewHolder> {

    private List<KpSongs> favoriteSongList;

    public AdpterforFavorites(List<KpSongs> favoriteSongList) {
        this.favoriteSongList = favoriteSongList;
    }

    @NonNull
    @Override
    public FavoriteSongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.favorite_song_item, parent, false);
        return new FavoriteSongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteSongViewHolder holder, int position) {
        KpSongs favoriteSong = favoriteSongList.get(position);
        holder.favoriteTitleTextView.setText(favoriteSong.getTitle());
        holder.favoriteDurationTextView.setText(favoriteSong.getDuration());
        holder.favoriteAlbumTextView.setText(favoriteSong.getAlbumName());
        // Set album cover image using Picasso or Glide library or by loading the image from URL
        // Example: Glide.with(holder.itemView.getContext()).load(favoriteSong.getAlbumCover()).into(holder.favoriteAlbumCoverImageView);
    }

    @Override
    public int getItemCount() {
        return favoriteSongList.size();
    }

    static class FavoriteSongViewHolder extends RecyclerView.ViewHolder {
        TextView favoriteTitleTextView, favoriteDurationTextView, favoriteAlbumTextView;

        public FavoriteSongViewHolder(@NonNull View itemView) {
            super(itemView);
            favoriteTitleTextView = itemView.findViewById(R.id.favoriteTitleTextView);
            favoriteDurationTextView = itemView.findViewById(R.id.favoriteDurationTextView);
            favoriteAlbumTextView = itemView.findViewById(R.id.favoriteAlbumTextView);
        }
    }
}
