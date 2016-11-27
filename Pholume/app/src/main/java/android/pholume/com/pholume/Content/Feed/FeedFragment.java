package android.pholume.com.pholume.Content.Feed;

import android.content.Context;
import android.os.Bundle;
import android.pholume.com.pholume.Model.FeedItem;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.Network.RestManager;
import android.pholume.com.pholume.PholumeMediaPlayer;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


public class FeedFragment extends Fragment {

    private final static String LOG = FeedFragment.class.getSimpleName();
    private Context context;
    private View rootView;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;

    private FeedListAdapter adapter;
    private ArrayList<FeedItem> pholumeList;

    public static PholumeMediaPlayer mediaPlayer;
    private static String audioUrl;

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        try {
            mediaPlayer.destroy();
        } catch (Exception e) {
            Log.e(LOG, "Couldnt stop MediaPlayer onPause");
        }
        mediaPlayer = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        context = getContext();
        rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        refreshLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_refresh_layout);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchFeed();
            }
        });
        pholumeList = new ArrayList<>();
        adapter = new FeedListAdapter(getActivity(), this, pholumeList);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        fetchFeed();

        return rootView;
    }

    public void playAudio(String url) {
        try {
            if (FeedFragment.mediaPlayer != null && FeedFragment.mediaPlayer.isPlaying()) {
                if (audioUrl.equals(url)) {
                    mediaPlayer.stop();
                } else {
                    mediaPlayer.destroy();
                    audioUrl = url;
                    mediaPlayer = PholumeMediaPlayer.create(context, url);
                }
            } else {
                audioUrl = url;
                mediaPlayer = PholumeMediaPlayer.create(context, url);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
