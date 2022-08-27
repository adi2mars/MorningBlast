package com.example.morningblastv1;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

import static com.example.morningblastv1.UseSpotify.artist;
import static com.example.morningblastv1.UseSpotify.chosen;
import static com.example.morningblastv1.UseSpotify.finalURI;
import static com.example.morningblastv1.UseSpotify.play2;
import static com.example.morningblastv1.UseSpotify.spinner;
import static com.example.morningblastv1.UseSpotify.track;

public class WordListAdapter3 extends
        RecyclerView.Adapter<WordListAdapter3.WordViewHolder>  {
    private final LinkedList<String> mWordList3;
    private LayoutInflater mInflater;
    public static String chosenPlaylist;
    public WordListAdapter3(Context context,
                            LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList3 = wordList;
    }


    @NonNull
    @Override
    public WordListAdapter3.WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mInflater.inflate(R.layout.wordlist2_item,
                viewGroup, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter3.WordViewHolder holder, int position) {
        String mCurrent = mWordList3.get(position);
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mWordList3.size();
    }
    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView wordItemView;
        final WordListAdapter3 mAdapter;



        public WordViewHolder(View itemView, WordListAdapter3 adapter) {
            super(itemView);
            wordItemView = itemView.findViewById(R.id.labPlaylist);
            this.mAdapter = adapter;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // Get the position of the item that was clicked.
            int mPosition = getLayoutPosition();
// Use that to access the affected item in mWordList.
            String element = mWordList3.get(mPosition);
            chosen.setText(element);
            if(spinner.getSelectedItem().toString().contains("Playlist")){
                finalURI = play2[mPosition].getUri();
            }else if(spinner.getSelectedItem().toString().contains("Artist")){
                finalURI = artist[mPosition].getUri();
            }else{
                finalURI = track[mPosition].getUri();
            }
            Log.d("MyActivity", "Picked: " + finalURI);
            //chosenPlaylist = element;
// Change the word in the mWordList.
// Notify the adapter, that the data has changed so it can
// update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }

}