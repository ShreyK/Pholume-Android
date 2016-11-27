package android.pholume.com.pholume.Content.Common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Content.Profile.ProfileActivity;
import android.pholume.com.pholume.Model.CapturedPholume;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.PrefManager;
import android.pholume.com.pholume.R;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.io.File;

public class PholumeBinder {

    private final static String LOG = PholumeBinder.class.getSimpleName();
    private Context context;
    private Activity mActivity;
    private Pholume pholume;
    private User user;

    private Bitmap image;
    private Drawable heart;
    private Drawable heartFilled;

    public PholumeBinder(Activity activity) {
        this.mActivity = activity;
        this.context = activity;
        heart = context.getDrawable(R.drawable.ic_heart);
        heartFilled = context.getDrawable(R.drawable.ic_heart_filled);
    }

    public void bind(ImageView pholumeImage,
                     TextView textView,
                     final Pholume pholume,
                     final User user,
                     final PholumeCallback<Pholume> likeCallback) {
        this.pholume = pholume;
        this.user = user;

        resizeImage(pholumeImage.getLayoutParams(), pholume.width, pholume.height);
        String url = Constants.BASE_PHOTO + pholume.photoUrl;
        Picasso.with(context).load(url).fit().centerCrop().into(pholumeImage);

//        setupUser();
        setupTitle(textView);
//        updateLikes();
//        updateComments();
//        bindListeners(likeCallback);
    }

    public void bind(final ImageView image, final EditText editText, CapturedPholume pholume) {
        this.user = PrefManager.getInstance().getCurrentUser();

        //get image from file
        resizeImage(image.getLayoutParams(), pholume.width, pholume.height);
        File imageFile = new File(pholume.photoUrl);
        Picasso.with(context).load(imageFile).fit().centerCrop().into(image);

        //get audio from file
        setupTitle(editText);
    }

//    private void setupUser() {
//        String avatarUrl = Constants.BASE_AVATAR + user.avatar;
//        Picasso.with(context).load(avatarUrl).fit().centerCrop().transform(new CircleTransform())
//                .placeholder(R.drawable.ic_profile)
//                .into(binding.userImage);
//        binding.pholumeUser.setText("@" + user.username);
//    }
//
//    private void setupTime() {
//        String time = Util.getTimeSince(pholume.dateCreated, context);
//        binding.pholumeTime.setText(time);
//    }

    private ViewGroup.LayoutParams resizeImage(final ViewGroup.LayoutParams lparams, final int width, final int height) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int MAX_HEIGHT = (int) Math.round(metrics.widthPixels * 1.3);
        ViewGroup.LayoutParams lp = lparams;
        int ratio = width / height;
        if (ratio < 1) {
            if (height > MAX_HEIGHT) {
                lp.height = MAX_HEIGHT;
            } else {
                lp.height = height;
            }
        }
        return lp;
    }

//    private void bindListeners(final PholumeCallback<Pholume> likeCallback) {
//        binding.likeButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                RestManager.getInstance().postLike(pholume.id, likeCallback);
//            }
//        });
//        binding.commentButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (context == null) return;
//                Intent intent = new Intent(context, CommonListActivity.class);
//                intent.putExtra(CommonListActivity.TYPE_EXTRA, CommonListActivity.COMMENTS);
//                intent.putExtra(CommonListActivity.PHOLUME_EXTRA, pholume);
//                context.startActivity(intent);
//            }
//        });
//    }

    private void setupTitle(View text) {
        if (text instanceof EditText) {
            EditText editText = (EditText) text;
            editText.setEnabled(true);
            editText.setFocusable(true);
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setHint("Set Description");
            editText.setText("");
            editText.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            TextView textView = (TextView) text;
            textView.setEnabled(false);
            textView.setFocusable(false);
            textView.setBackground(null);
            textView.setPadding(0, 0, 0, 0);
            textView.setInputType(InputType.TYPE_NULL);
            textView.setText(pholume.description);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
//            binding.pholumeFooter.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    startUserActivity();
//                }
//            });
        }
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
//        binding.commentButton.setText(pholume.getNumberOfComments());
    }

    public void updateLikes() {
//        if (binding == null || pholume == null) return;
//        binding.likeCounterButton.setText(pholume.getNumberOfLikes());
//        updateLikeImage();
    }

    public void updateLikeImage() {
//        if (pholume.likes.contains(PrefManager.getInstance().getCurrentUser().id)) {
//            binding.likeButton.setImageDrawable(heartFilled);
//        } else {
//            binding.likeButton.setImageDrawable(heart);
//        }
    }
}
