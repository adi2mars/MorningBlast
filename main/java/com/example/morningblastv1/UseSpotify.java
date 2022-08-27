package com.example.morningblastv1;

import android.content.Intent;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.SpotifyHttpManager;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.model_objects.specification.PlaylistSimplified;
import com.wrapper.spotify.model_objects.specification.Track;
import com.wrapper.spotify.requests.data.player.StartResumeUsersPlaybackRequest;
import com.wrapper.spotify.requests.data.playlists.GetListOfCurrentUsersPlaylistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchPlaylistsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchTracksRequest;

import java.net.URI;
import java.util.LinkedList;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import static com.spotify.sdk.android.authentication.AuthenticationResponse.Type.TOKEN;
import static com.spotify.sdk.android.authentication.LoginActivity.REQUEST_CODE;
interface setText{
    void setPlay(TextView txt, String text);
}
public class UseSpotify extends AppCompatActivity  {
    private static final String REDIRECT_URI = "com.morningblastv1://callback";
    private static final URI redirectUri = SpotifyHttpManager.makeUri(REDIRECT_URI);
    private static final String code = "";
    public static String finalURI = "None";
    private static final String refreshToken = "b0KuPuLw77Z0hQhCsK-GTHoEx_kethtn357V7iqwEpCTIsLgqbBC_vQBTGC6M5rINl0FrqHK-D3cbOsMOlfyVKuQPvpyGcLcxAoLOTpYXc28nVwB7iBq2oKj9G9lHkFOUKn";
    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()

            .setClientId("23293212525c40958dbc061ac7bf3cf8")
            .setClientSecret("4c88a2b3746b41f9bb81cb0194567939")
            .setRedirectUri(redirectUri)
            .setRefreshToken(refreshToken)
            .build();
    private static GetListOfCurrentUsersPlaylistsRequest getListOfCurrentUsersPlaylistsRequest;
    public static PlaylistSimplified[] play;
    public static Track[] track;
    public static Artist[] artist;
    TextView testText;
    private RecyclerView mRecyclerView;
    private WordListAdapter2 mAdapter;
    private final LinkedList<String> mWordList2 = new LinkedList<>();
    private RecyclerView mRecyclerView2;
    private WordListAdapter3 mAdapter2;
    private final LinkedList<String> mWordList3 = new LinkedList<>();
    public static TextView chosen;
    ConstraintLayout layout;
    public static PlaylistSimplified[] play2;
    private static SearchPlaylistsRequest searchPlaylistsRequest;
    private static SearchTracksRequest searchTracksRequest;
    private static SearchArtistsRequest searchArtistsRequest;
    private static StartResumeUsersPlaybackRequest startResumeUsersPlaybackRequest;

    EditText userInput;
    EditText urlInput;
    public static Spinner spinner;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_use_spotify);
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder("23293212525c40958dbc061ac7bf3cf8", TOKEN, REDIRECT_URI);

        builder.setScopes(new String[]{"user-read-private", "streaming"});
        AuthenticationRequest request = builder.build();

        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
        testText = findViewById(R.id.labUser);
        // Get a handle to the RecyclerView.
        mRecyclerView = findViewById(R.id.recyclerView2);
// Create an adapter and supply the data to be displayed.
        mAdapter = new WordListAdapter2(this, mWordList2);
// Connect the adapter with the RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
// Give the RecyclerView a default layout manager.
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView2 = findViewById(R.id.recyclerView3);
// Create an adapter and supply the data to be displayed.
        mAdapter2 = new WordListAdapter3(this, mWordList3);
// Connect the adapter with the RecyclerView.
        mRecyclerView2.setAdapter(mAdapter2);
// Give the RecyclerView a default layout manager.
        mRecyclerView2.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView2.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        chosen = findViewById(R.id.txtFinalPlaylist);
         spinner = (Spinner) findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.types_array, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        userInput = findViewById(R.id.userInput);
        urlInput = findViewById(R.id.urlInput);


    }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Check if result comes from the correct activity
        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case TOKEN:
                    Log.d("MyActivity", "Success");

                    spotifyApi.setAccessToken(response.getAccessToken());

                    getListOfCurrentUsersPlaylistsRequest = spotifyApi
                            .getListOfCurrentUsersPlaylists()
//          .limit(10)
//          .offset(0)
                            .build();
                    play = getListOfCurrentUsersPlaylists_Async();
                   // startResumeUsersPlaybackRequest = spotifyApi
                         //   .startResumeUsersPlayback()
//          .context_uri("spotify:album:5zT1JLIj9E57p3e1rFm9Uq")
//          .device_id("5fbb3ba6aa454b5534c4ba43a8c7e8e45a63ad0e")
//          .offset(JsonParser.parseString("{\"uri\":\"spotify:track:01iyCAUm8EvOFqVWYJ3dVX\"}").getAsJsonObject())
//          .uris(JsonParser.parseString("[\"spotify:track:01iyCAUm8EvOFqVWYJ3dVX\"]").getAsJsonArray())
//          .position_ms(10000)
                      //      .build();
                    //startResumeUsersPlayback_Async();
                    //testText.setText(play[0].getName());
                    int wordListSize = mWordList2.size();
                    // Add a new word to the wordList.
                    for(int i =0;i<play.length;i++){
                        mWordList2.addLast(play[i].getName());
                        // Notify the adapter, that the data has changed.
                    }
                    mRecyclerView.getAdapter().notifyItemInserted(wordListSize);
                    // Scroll to the bottom.
                    mRecyclerView.smoothScrollToPosition(wordListSize);
                    mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                            DividerItemDecoration.VERTICAL));

                    break;

                // Auth flow returned an error
                case ERROR:
                    // Handle error response
                    break;

                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }
    public static void startResumeUsersPlayback_Async() {
        try {
            final CompletableFuture<String> stringFuture = startResumeUsersPlaybackRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final String string = stringFuture.join();

            System.out.println("Null: " + string);
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
        }
    }
    public  PlaylistSimplified[] getListOfCurrentUsersPlaylists_Async() {
        PlaylistSimplified[]playlists;
        try {
            final CompletableFuture<Paging<PlaylistSimplified>> pagingFuture = getListOfCurrentUsersPlaylistsRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = pagingFuture.join();
            playlists = playlistSimplifiedPaging.getItems();
            for(int i =0;i<playlists.length;i++){
                PlaylistSimplified play = playlists[i];
                Log.d("MyActivity", "getID"+i+"    " +play.getName());
            }

            Log.d("MyActivity","Total: " + playlistSimplifiedPaging.getTotal());
        } catch (CompletionException e) {
            Log.d("MyAcitivity","Error: " + e.getMessage());
            playlists = new PlaylistSimplified[0];
        } catch (CancellationException e) {
            Log.d("MyAcitivity","AsyncOperation Cancel");
            playlists = new PlaylistSimplified[0];
        }
        return playlists;
    }
    public PlaylistSimplified[] searchPlaylists_Async() {
        PlaylistSimplified[]playlists;
        try {
            final CompletableFuture<Paging<PlaylistSimplified>> pagingFuture = searchPlaylistsRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Paging<PlaylistSimplified> playlistSimplifiedPaging = pagingFuture.join();
            playlists = playlistSimplifiedPaging.getItems();
            for(int i =0;i<playlists.length;i++){
                PlaylistSimplified play = playlists[i];
                Log.d("MyActivity", "getID"+i+"    " +play.getName());
            }

            Log.d("MyActivity","Total: " + playlistSimplifiedPaging.getTotal());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
            playlists = new PlaylistSimplified[0];

        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
            playlists = new PlaylistSimplified[0];
        }
        return playlists;
    }
    public Track[] searchTracks_Async() {
        Track[]tracks;
        try {
            final CompletableFuture<Paging<Track>> pagingFuture = searchTracksRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Paging<Track> trackPaging = pagingFuture.join();
            tracks = trackPaging.getItems();
            for(int i =0;i<tracks.length;i++){
                Track play = tracks[i];
                Log.d("MyActivity", "getID"+i+"    " +play.getName() + " " + play.getAlbum().getName() + " " + play.getId());
            }

            System.out.println("Total: " + trackPaging.getTotal());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
            tracks = new Track[0];

        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
            tracks = new Track[0];

        }
        return tracks;
    }
    public Artist[] searchArtists_Async() {
        Artist[] artists;
        try {
            final CompletableFuture<Paging<Artist>> pagingFuture = searchArtistsRequest.executeAsync();

            // Thread free to do other tasks...

            // Example Only. Never block in production code.
            final Paging<Artist> artistPaging = pagingFuture.join();
            artists = artistPaging.getItems();
            for(int i =0;i<artists.length;i++){
                Artist play = artists[i];
              //  Log.d("MyActivity", "getID"+i+"    " +play.getName() + " " + play.getAlbum().getName() + " " + play.getId());
            }
            System.out.println("Total: " + artistPaging.getTotal());
        } catch (CompletionException e) {
            System.out.println("Error: " + e.getCause().getMessage());
            artists = new Artist[0];

        } catch (CancellationException e) {
            System.out.println("Async operation cancelled.");
            artists = new Artist[0];
        }
        return artists;
    }



    public void Search(View view) {
        String text = spinner.getSelectedItem().toString();
        int size = mWordList3.size();
        mWordList3.clear();
        mRecyclerView2.getAdapter().notifyItemRangeRemoved(0, size);
        String q = userInput.getText().toString();
        if (text.contains("Playlist")) {
            searchPlaylistsRequest = spotifyApi.searchPlaylists(q)
//          .market(CountryCode.SE)
//          .limit(10)
//          .offset(0)
//          .includeExternal("audio")
                    .build();
            play2 = searchPlaylists_Async();
            int wordListSize2 = mWordList3.size();
            // Add a new word to the wordList.
            for (int i = 0; i < play2.length; i++) {
                String title = play2[i].getName() + " - " + play2[i].getOwner().getDisplayName();
                mWordList3.addLast(title);
                // Notify the adapter, that the data has changed.
            }
            mRecyclerView2.getAdapter().notifyItemInserted(wordListSize2);
            // Scroll to the bottom.
            //mRecyclerView2.smoothScrollToPosition(wordListSize2);
        }else if(text.contains("Track")){
            searchTracksRequest = spotifyApi.searchTracks(q)
//          .market(CountryCode.SE)
//          .limit(10)
//          .offset(0)
//          .includeExternal("audio")
                    .build();
            track = searchTracks_Async();
            int wordListSize2 = mWordList3.size();
            // Add a new word to the wordList.
            for (int i = 0; i < track.length; i++) {
                String title = track[i].getName() + " - " + track[i].getArtists()[0].getName();
                mWordList3.addLast(title);
                // Notify the adapter, that the data has changed.
            }
            mRecyclerView2.getAdapter().notifyItemInserted(wordListSize2);
        }else if(text.contains("Artist")){
            searchArtistsRequest = spotifyApi.searchArtists(q)
//          .market(CountryCode.SE)
//          .limit(10)
//          .offset(0)
//          .includeExternal("audio")
                    .build();
        artist = searchArtists_Async();
            int wordListSize2 = mWordList3.size();
            // Add a new word to the wordList.
            for (int i = 0; i < artist.length; i++) {
                String title = artist[i].getName();
                mWordList3.addLast(title);
                // Notify the adapter, that the data has changed.
            }
            mRecyclerView2.getAdapter().notifyItemInserted(wordListSize2);
        }
    }

    public void useURL(View view) {
        String url = urlInput.getText().toString();
        Log.d("MyActivity", "Task" + ": " + url);

        String[]splits = url.split(".com");
        for(int i =0; i<splits.length; i++){
            Log.d("MyActivity", i + ": " + splits[i]);
        }
        String next = splits[1];
        String[]split2 = next.split("/");
        for(int i =0; i<split2.length; i++){
            Log.d("MyActivity", i + ": " + split2[i]);
        }
        String type = split2[1];
        String[]split3 = split2[2].split("\\?");
        for(int i =0; i<split3.length; i++){
            Log.d("MyActivity", i + ": " + split3[i]);
        }
        String id = split3[0];
        String uri = "spotify:"+type+":"+id;
        Log.d("MyActivity", "uri" + ": " + uri);
        chosen.setText("URL USED");
        finalURI = uri;

    }

    public void Clear(View view) {
        chosen.setText("None");
    }

    public void SaveURI(View view) {
        Intent replyIntent = new Intent();
        replyIntent.putExtra("FinalURI",finalURI);
        setResult(1, replyIntent);
        finish();
    }

    public void onCheckboxClicked(View view) {
        CheckBox checkBox = findViewById(R.id.checkBoxNews);
        if(checkBox.isChecked()){
            searchPlaylistsRequest = spotifyApi.searchPlaylists("Daily Drive")
//          .market(CountryCode.SE)
//          .limit(10)
//          .offset(0)
//          .includeExternal("audio")
                    .build();
            play2 = searchPlaylists_Async();
            finalURI = play2[0].getUri() + "News";
            chosen.setText("News");
        }else{
            finalURI = "None";
            chosen.setText("None");
        }
    }
}