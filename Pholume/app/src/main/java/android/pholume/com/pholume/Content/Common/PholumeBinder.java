package android.pholume.com.pholume.Content.Common;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Content.Profile.ProfileActivity;
import android.pholume.com.pholume.Content.Views.CircleTransform;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.Network.RestManager;
import android.pholume.com.pholume.PrefManager;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.Util;
import android.pholume.com.pholume.databinding.CardPholumeFeedBinding;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

public class PholumeBinder {

    private Context context;
    private CardPholumeFeedBinding binding;
    private Pholume pholume;
    private User user;

    private Drawable heart;
    private Drawable heartFilled;

    public PholumeBinder(Context context) {
        this.context = context;
        heart = context.getDrawable(R.drawable.ic_heart);
        heartFilled = context.getDrawable(R.drawable.ic_heart_filled);
    }

    public void bind(CardPholumeFeedBinding binding, final Pholume pholume, final User user,
                     final PholumeCallback<Pholume> likeCallback) {
        this.binding = binding;
        this.pholume = pholume;
        this.user = user;
        String url = Constants.BASE_PHOTO + pholume.photoUrl;

        //image resizing;
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int MAX_HEIGHT = (int) Math.round(metrics.widthPixels*1.3);
        ViewGroup.LayoutParams lp = binding.pholumeImage.getLayoutParams();
        int ratio = pholume.width/pholume.height;
        if(ratio < 1){
            if(pholume.height > MAX_HEIGHT){
                lp.height = MAX_HEIGHT;
            } else {
                lp.height = pholume.height;
            }
        }
        binding.pholumeImage.setLayoutParams(lp);
        //show image
        Picasso.with(context).load(url).fit().centerCrop().into(binding.pholumeImage);

        //update like image
        updateLikeImage();

        //set avatar
        String avatarUrl = Constants.BASE_AVATAR + user.avatar;
        Picasso.with(context).load(avatarUrl).fit().centerCrop().transform(new CircleTransform())
                .placeholder(R.drawable.ic_profile)
                .into(binding.userImage);

        //set texts
        binding.pholumeTitle.setText(pholume.description);
        binding.pholumeUser.setText(user.username);
        binding.pholumeTime.setText(Util.getTimeSince(pholume.dateCreated, context));

        //like button
        binding.likeButton.setText(pholume.getNumberOfLikes());
        binding.likeButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestManager.getInstance().postLike(pholume.id, likeCallback);
            }
        });

        //comment button
        binding.commentButton.setText(pholume.getNumberOfComments());
        binding.commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context == null) return;
                Intent intent = new Intent(context, CommonListActivity.class);
                intent.putExtra(CommonListActivity.TYPE_EXTRA, CommonListActivity.COMMENTS);
                intent.putExtra(CommonListActivity.PHOLUME_EXTRA, pholume);
                context.startActivity(intent);
            }
        });

        binding.pholumeHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });
    }

    public void updateLikeImage(){
        if(pholume.likes.contains(PrefManager.getInstance().getCurrentUser().id)){
            binding.likeButton.setCompoundDrawablesWithIntrinsicBounds(heartFilled, null, null, null);
        } else {
            binding.likeButton.setCompoundDrawablesWithIntrinsicBounds(heart, null, null, null);
        }
    }

    public void updatePholume(Pholume pholume) {
        this.pholume = pholume;
    }

    public void updateLikes() {
        if (binding == null || pholume == null) return;
        binding.likeButton.setText(pholume.getNumberOfLikes());
    }
}