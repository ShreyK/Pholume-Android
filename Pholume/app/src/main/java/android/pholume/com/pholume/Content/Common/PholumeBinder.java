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
import android.text.InputType;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.squareup.picasso.Picasso;

import java.io.File;

import static android.pholume.com.pholume.Content.Common.CommonListActivity.LOG;

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

    public void bind(CardPholumeFeedBinding binding,
                     final Pholume pholume,
                     final User user,
                     final PholumeCallback<Pholume> likeCallback) {
        this.binding = binding;
        this.pholume = pholume;
        this.user = user;

        setupUser();
        setupPholume();
        setupTitle(false);
        updateLikes();
        updateComments();
        bindListeners(likeCallback);
    }

    public void bind(final CardPholumeFeedBinding binding,
                     String imageFilePath,
                     String audioFile,
                     int imageWidth,
                     int imageHeight) {
        this.binding = binding;
        this.user = PrefManager.getInstance().getCurrentUser();

        binding.likeButton.setVisibility(View.GONE);
        binding.commentButton.setVisibility(View.GONE);

        File imageFile = new File(imageFilePath);
        resizeImage(imageWidth, imageHeight);
        //get image from file
        Picasso.with(context).load(imageFile).fit().centerCrop().into(binding.pholumeImage);

        //get audio from file
        setupUser();
        setupTitle(true);
    }

    private void setupUser() {
        String avatarUrl = Constants.BASE_AVATAR + user.avatar;
        Picasso.with(context).load(avatarUrl).fit().centerCrop().transform(new CircleTransform())
                .placeholder(R.drawable.ic_profile)
                .into(binding.userImage);
        binding.pholumeUser.setText("@" + user.username);
    }

    private void setupTime() {
        String time = Util.getTimeSince(pholume.dateCreated, context);
        binding.pholumeTime.setText(time);
    }

    private void resizeImage(int width, int height) {
        Log.d(LOG, width + " " + height);
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int MAX_HEIGHT = (int) Math.round(metrics.widthPixels * 1.3);
        ViewGroup.LayoutParams lp = binding.pholumeImage.getLayoutParams();
        int ratio = width / height;
        if (ratio < 1) {
            if (height > MAX_HEIGHT) {
                lp.height = MAX_HEIGHT;
            } else {
                lp.height = height;
            }
        }
        binding.pholumeImage.setLayoutParams(lp);
    }

    private void setupPholume() {
        String url = Constants.BASE_PHOTO + pholume.photoUrl;
        //image resizing;
        resizeImage(pholume.width, pholume.height);
        //show image
        Picasso.with(context).load(url).fit().centerCrop().into(binding.pholumeImage);

    }

    private void bindListeners(final PholumeCallback<Pholume> likeCallback) {
        binding.likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestManager.getInstance().postLike(pholume.id, likeCallback);
            }
        });
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
    }

    private void setupTitle(boolean editable) {
        if (editable) {
            binding.pholumeTitle.setEnabled(true);
            binding.pholumeTitle.setFocusable(true);
            binding.pholumeTitle.setInputType(InputType.TYPE_CLASS_TEXT);
            binding.pholumeTitle.setHint("Set Description");
            binding.pholumeTitle.setText("");
        } else {
            binding.pholumeTitle.setEnabled(false);
            binding.pholumeTitle.setFocusable(false);
            binding.pholumeTitle.setBackground(null);
            binding.pholumeTitle.setPadding(0, 0, 0, 0);
            binding.pholumeTitle.setInputType(InputType.TYPE_NULL);
            binding.pholumeTitle.setText(pholume.description);
        }
        binding.pholumeTitle.setGravity(Gravity.CENTER_HORIZONTAL);
        binding.pholumeFooter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUserActivity();
            }
        });
    }

    private void startUserActivity() {
        Intent intent = new Intent(context, ProfileActivity.class);
        intent.putExtra("user", user);
        context.startActivity(intent);
    }

    public void updatePholume(Pholume pholume) {
        this.pholume = pholume;
    }

    public void updateComments() {
        binding.commentButton.setText(pholume.getNumberOfComments());
    }

    public void updateLikes() {
        if (binding == null || pholume == null) return;
        binding.likeCounterButton.setText(pholume.getNumberOfLikes());
        updateLikeImage();
    }

    public void updateLikeImage() {
        if (pholume.likes.contains(PrefManager.getInstance().getCurrentUser().id)) {
            binding.likeButton.setImageDrawable(heartFilled);
        } else {
            binding.likeButton.setImageDrawable(heart);
        }
    }
}
