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

import static com.example.morningblastv1.UseSpotify.chosen;
import static com.example.morningblastv1.UseSpotify.finalURI;
import static com.example.morningblastv1.UseSpotify.play;

public class WordListAdapter2 extends
        RecyclerView.Adapter<WordListAdapter2.WordViewHolder>  {
    private final LinkedList<String> mWordList2;
    private LayoutInflater mInflater;
    public static String chosenPlaylist;
    public WordListAdapter2(Context context,
                            LinkedList<String> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList2 = wordList;
    }


    @NonNull
    @Override
    public WordListAdapter2.WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mInflater.inflate(R.layout.wordlist2_item,
                viewGroup, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter2.WordViewHolder holder, int position) {
        String mCurrent = mWordList2.get(position);
        holder.wordItemView.setText(mCurrent);
    }

    @Override
    public int getItemCount() {
        return mWordList2.size();
    }
    class WordViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public final TextView wordItemView;
        final WordListAdapter2 mAdapter;



        public WordViewHolder(View itemView, WordListAdapter2 adapter) {
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
            String element = mWordList2.get(mPosition);
            chosen.setText(element);
            finalURI = play[mPosition].getUri();
            Log.d("MyActivity", "Picked: " + finalURI);
            //chosenPlaylist = element;
// Change the word in the mWordList.
// Notify the adapter, that the data has changed so it can
// update the RecyclerView to display the data.
            mAdapter.notifyDataSetChanged();
        }
    }

}