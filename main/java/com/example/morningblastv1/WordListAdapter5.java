package com.example.morningblastv1;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class WordListAdapter5 extends
        RecyclerView.Adapter<WordListAdapter5.WordViewHolder>  {
    private final LinkedList<Model3> mWordList5;
    private LayoutInflater mInflater;
    public static String chosenPlaylist;
    public WordListAdapter5(Context context,
                            LinkedList<Model3> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList5 = wordList;
    }


    @NonNull
    @Override
    public WordListAdapter5.WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mInflater.inflate(R.layout.wordlist4_item,
                viewGroup, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter5.WordViewHolder holder, int position) {
       holder.headline.setText(mWordList5.get(position).headline);
       holder.source.setText(mWordList5.get(position).source);

    }

    @Override
    public int getItemCount() {
        return mWordList5.size();
    }
    class WordViewHolder extends RecyclerView.ViewHolder{
        TextView headline;
        TextView source;


        public WordViewHolder(View itemView, WordListAdapter5 adapter) {
            super(itemView);
            headline = itemView.findViewById(R.id.txtHeadline);
            source = itemView.findViewById(R.id.txtSource);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = mWordList5.get(getAdapterPosition()).url;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    v.getContext().startActivity(i);
                }
            });
        }


    }

}