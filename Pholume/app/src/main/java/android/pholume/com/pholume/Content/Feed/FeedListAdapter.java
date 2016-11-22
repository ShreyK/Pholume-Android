package android.pholume.com.pholume.Content.Feed;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Content.Common.BaseListAdapter;
import android.pholume.com.pholume.Content.Common.PholumeBinder;
import android.pholume.com.pholume.Model.FeedItem;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.CardPholumeFeedBinding;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


class FeedListAdapter extends BaseListAdapter<FeedListAdapter.ViewHolder, FeedItem> {

    private PholumeBinder binder;
    private Drawable volumeOff;
    private Drawable volumeOn;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardPholumeFeedBinding binding;
        boolean hasLiked;

        ViewHolder(CardPholumeFeedBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            hasLiked = false;
        }
    }

    FeedListAdapter(Context context, List<FeedItem> list) {
        super(context, list);
        binder = new PholumeBinder(context);
        volumeOff = context.getDrawable(R.drawable.ic_volume_off);
        volumeOn = context.getDrawable(R.drawable.ic_volume_on);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardPholumeFeedBinding binding = CardPholumeFeedBinding.inflate(LayoutInflater.from(context)
                , parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FeedItem feedItem = list.get(position);
        final Pholume pholume = feedItem.pholume;
        final User user = feedItem.user;
        final CardPholumeFeedBinding binding = holder.binding;

        final PholumeCallback<Pholume> likeCallback = new PholumeCallback<Pholume>("Like") {
            @Override
            public void onResponse(Call<Pholume> call, Response<Pholume> response) {
                if (response.isSuccessful()) {
                    feedItem.pholume = response.body();
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    Toast.makeText(context,
                            response.code() + ": " + response.message(),
                            Toast.LENGTH_SHORT)
                            .show();
                    Log.e("like", response.message());
                }
            }
        };
        binding.volumeImage.setVisibility(View.GONE);
        binder.bind(binding, pholume, user, likeCallback);

        //set image listener
        binding.pholumeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FeedFragment.playAudio(Constants.BASE_AUDIO + pholume.audioUrl);
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Couldnt find Pholume audio :(", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }
}
