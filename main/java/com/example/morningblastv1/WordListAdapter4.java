package com.example.morningblastv1;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.LinkedList;

public class WordListAdapter4 extends
        RecyclerView.Adapter<WordListAdapter4.WordViewHolder>  {
    private final LinkedList<Model2> mWordList4;
    private LayoutInflater mInflater;
    public static String chosenPlaylist;
    public WordListAdapter4(Context context,
                            LinkedList<Model2> wordList) {
        mInflater = LayoutInflater.from(context);
        this.mWordList4 = wordList;
    }


    @NonNull
    @Override
    public WordListAdapter4.WordViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View mItemView = mInflater.inflate(R.layout.wordlist3_item,
                viewGroup, false);
        return new WordViewHolder(mItemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull WordListAdapter4.WordViewHolder holder, int position) {
        holder.temp.setText(mWordList4.get(position).temp);
        holder.feelsLike.setText(mWordList4.get(position).feelsLike);
        holder.descrip.setText(mWordList4.get(position).descrip);
        holder.hour.setText(mWordList4.get(position).hour);


    }

    @Override
    public int getItemCount() {
        return mWordList4.size();
    }
    class WordViewHolder extends RecyclerView.ViewHolder{
    TextView temp;
    TextView feelsLike;
    TextView descrip;
    TextView hour;


        public WordViewHolder(View itemView, WordListAdapter4 adapter) {
            super(itemView);
            temp = itemView.findViewById(R.id.txtTemp);
            feelsLike = itemView.findViewById(R.id.txtFeelLike);
            descrip = itemView.findViewById(R.id.txtDescrip);
            hour = itemView.findViewById(R.id.txtHour);

        }


    }

}