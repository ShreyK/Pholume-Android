package android.pholume.com.pholume.Content.Feed;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Content.Common.BaseListAdapter;
import android.pholume.com.pholume.Content.Common.PholumeBinder;
import android.pholume.com.pholume.Model.FeedItem;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.PholumeViewBinding;
import android.pholume.com.pholume.databinding.PholumeViewContainerBinding;
import android.pholume.com.pholume.databinding.PholumeViewFooterBinding;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Response;


class FeedListAdapter extends BaseListAdapter<FeedListAdapter.ViewHolder, FeedItem> {

    private PholumeBinder binder;
    private Drawable volumeOff;
    private Drawable volumeOn;
    private FeedFragment fragment;

    static class ViewHolder extends RecyclerView.ViewHolder {

        PholumeViewBinding binding;
        boolean hasLiked;

        ViewHolder(PholumeViewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            hasLiked = false;
        }
    }

    FeedListAdapter(Activity context, FeedFragment fragment, List<FeedItem> list) {
        super(context, list);
        this.fragment = fragment;
        binder = new PholumeBinder(context);
        volumeOff = context.getDrawable(R.drawable.ic_volume_off);
        volumeOn = context.getDrawable(R.drawable.ic_volume_on);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        PholumeViewBinding binding = PholumeViewBinding.inflate(LayoutInflater.from(context)
                , parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final FeedItem feedItem = list.get(position);
        final Pholume pholume = feedItem.pholume;
        final User user = feedItem.user;
        final PholumeViewContainerBinding containerBinding = holder.binding.pholumeContainer;
        final PholumeViewFooterBinding footerBinding = holder.binding.pholumeFooter;

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
        containerBinding.volumeImage.setVisibility(View.GONE);
        binder.bind(containerBinding, footerBinding, pholume, user, likeCallback);

        //set image listener
        containerBinding.pholumeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String audioUrl = Constants.BASE_AUDIO + pholume.audioUrl;
                fragment.playAudio(audioUrl);
            }
        });
    }
}
