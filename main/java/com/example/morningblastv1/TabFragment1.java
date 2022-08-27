package com.example.morningblastv1;


import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedList;

import static android.content.Context.MODE_PRIVATE;
import static com.example.morningblastv1.MainActivity.position;

/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment1 extends Fragment {
    public static LinkedList<Model> mWordList = new LinkedList<>();
    private RecyclerView mRecyclerView;
    private WordListAdapter mAdapter;
    public static SharedPreferences mPreferences;

    private String sharedPrefFile =    "com.example.morningblastv1";



    public TabFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment1, container, false);
        // Inflate the layout for this fragment
        // Get a handle to the RecyclerView.
        mPreferences = getContext().getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        if(mWordList.size()==0) {
            for (int i = 0; i < mPreferences.getInt("Size", -1); i++) {
                String time = mPreferences.getString("time" + i, "Error");
                Log.d("MyActivity", "saved22" + "" + mPreferences.getInt("Size", -1));

               String date = mPreferences.getString("date" + i, "Error");
                String title = mPreferences.getString("title" + i, "Error");
                boolean checked = mPreferences.getBoolean("checked" + i, false);
                long millis = mPreferences.getLong("calendar" + i, -1);
                int volume = mPreferences.getInt("volume"+i, 100);
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(millis);
               String arraylist = mPreferences.getString("dayNum" + i, "Error");
                String uri = mPreferences.getString("uri" + i, "None");
                Uri uriFinal;
                if(!uri.contains("None")){
                    uriFinal = Uri.parse(uri);
                }else{
                    uriFinal = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                }
                Log.d("MyActivity", "saved22      " + uri);

                String spotifyUri = mPreferences.getString("SpotifyURI" + i, "Error");
                arraylist = arraylist.replaceAll("[\\[\\](){}]","");
                ArrayList<Integer> daysList = new ArrayList();
                if(arraylist.contains(",")){
                    String [] array = arraylist.split(", ");
                    for(int j =0;j<array.length; j++){
                        // Log.d("MyActivity", "days:  " + array[j]);
                        daysList.add(Integer.parseInt(array[j]));
                    }
                }

                mWordList.add(new Model(time,date,title,checked,calendar,daysList, uriFinal,spotifyUri, volume));
                Log.d("MyActivity", "saved22      " + spotifyUri);

            }
        }
//        if(position!=-1){
//            if(mWordList.get(position).dayNum.isEmpty()) {
//                mWordList.get(position).checked = false;
//            }
//        }
        mRecyclerView = view.findViewById(R.id.recyclerView);
// Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter(view.getContext(), mWordList);
// Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL));
        mAdapter.setOnItemClickListener(new WordListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //int mPosition = mAdapter.getAdapterPosition();

                // Use that to access the affected item in mWordList.
                //String element = mWordList.get(position).task;
                // Change the word in the mWordList.

                //mWordList.set(position, "Clicked! " + element);
                // Notify the adapter, that the data has changed so it can
                // update the RecyclerView to display the data.
                //mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onDeleteClick(int position) {
                Log.d("MyActivity", "checked");
                mWordList.remove(position);
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount());
                /*int s = mPreferences.getInt("Size",0);
                if(s!=0){

                }*/
                /*View v = mRecyclerView.getChildAt(position);
                Log.d(Tag, "Removing" + position + (v==null));
                Log.d(Tag, "Removingi" + position + mWordList.get(position).priority);

                if(position<8) {
                    mRecyclerView.removeViewAt(position);
                }else{
                    int a =position-7;
                    mRecyclerView.removeViewAt(position-a);
                }
                mWordList.remove(position);
                //Log.d(Tag, "Adi123");
                mAdapter.notifyItemRemoved(position);
                mAdapter.notifyItemRangeChanged(position, mWordList.size());
                mAdapter.notifyDataSetChanged();


                //finish();
                //startActivity(getIntent());
                */

            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));

        return view;
    }


}
