package android.pholume.com.pholume.Content.Feed;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.pholume.com.pholume.Model.FeedItem;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.Network.RestManager;
import android.pholume.com.pholume.R;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class FeedFragment extends Fragment implements AudioManager.OnAudioFocusChangeListener {

    private View rootView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private FeedListAdapter adapter;
    private ArrayList<FeedItem> pholumeList;

    private static MediaPlayer mediaPlayer;
    private static String audioUrl;

    @Override
    public void onResume() {
        super.onResume();
        initMediaPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        destroyMediaPlayer();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeed();
            }
        });
        pholumeList = new ArrayList<>();
        adapter = new FeedListAdapter(getActivity(), pholumeList);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchFeed();

        return rootView;
    }

    private void fetchFeed() {
        RestManager.getInstance().getFeed(new PholumeCallback<List<FeedItem>>("GetFeed") {
            @Override
            public void onResponse(Call<List<FeedItem>> call, Response<List<FeedItem>> response) {
                if (response.isSuccessful()) {
                    refreshLayout.setRefreshing(false);
                    pholumeList = (ArrayList<FeedItem>) response.body();
                    adapter.setData(pholumeList);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(),
                            response.code() + ": " + response.message(),
                            Toast.LENGTH_SHORT)
                            .show();
                    Log.e("getFeed", response.message());
                }
            }
        });
    }

    protected static void initMediaPlayer() {
        if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setVolume(1.0f, 1.0f);
                mediaPlayer.start();
            }
        });
        mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
                mediaPlayer.reset();
                return false;
            }
        });
    }

    private void destroyMediaPlayer() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    public static boolean playAudio(String url) throws IOException {
        if (mediaPlayer == null) {
            return false;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            initMediaPlayer();
            if(audioUrl.equals(url)){
                return false;
            }
        }
        audioUrl = url;
        initMediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepareAsync();
        return true;
    }


    @Override
    public void onAudioFocusChange(int focusChange) {
        if (mediaPlayer == null) {
            initMediaPlayer();
        }
        switch (focusChange) {
            case AudioManager.AUDIOFOCUS_GAIN:
                // resume playback
                if (!mediaPlayer.isPlaying()) mediaPlayer.start();
                mediaPlayer.setVolume(1.0f, 1.0f);
                break;

            case AudioManager.AUDIOFOCUS_LOSS:
                // Lost focus for an unbounded amount of time
                // stop playback and release media player
                destroyMediaPlayer();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                // Lost focus for a short time, but we have to stop
                // playback. We don't release the media player because playback
                // is likely to resume
                if (mediaPlayer.isPlaying()) mediaPlayer.pause();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                // Lost focus for a short time, but it's ok to keep playing
                // at an attenuated level
                if (mediaPlayer.isPlaying()) mediaPlayer.setVolume(0.1f, 0.1f);
                break;
        }
    }
}
