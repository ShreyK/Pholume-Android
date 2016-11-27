package android.pholume.com.pholume.Content.Common;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.PholumeMediaPlayer;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.ActivityPholumeBinding;
import android.pholume.com.pholume.databinding.PholumeViewContainerBinding;
import android.pholume.com.pholume.databinding.PholumeViewFooterBinding;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import retrofit2.Call;
import retrofit2.Response;

public class PholumeActivity extends AppCompatActivity {

    private static final String LOG = PholumeActivity.class.getSimpleName();

    private Context context;
    private static PholumeMediaPlayer mediaPlayer;
    private Drawable volumeOff;
    private Drawable volumeOn;

    ActivityPholumeBinding binding;
    PholumeViewContainerBinding containerBinding;
    PholumeViewFooterBinding footerBinding;
    PholumeBinder binder;
    PholumeCallback<Pholume> likeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pholume);
        containerBinding = binding.pholumeView.pholumeContainer;
        footerBinding = binding.pholumeView.pholumeFooter;
        binder = new PholumeBinder(this);

        volumeOff = getDrawable(R.drawable.ic_volume_off);
        volumeOn = getDrawable(R.drawable.ic_volume_on);

        Bundle bundle = getIntent().getExtras();
        final Pholume pholume = bundle.getParcelable("pholume");
        final User user = bundle.getParcelable("user");

        likeCallback = new PholumeCallback<Pholume>("Like") {
            @Override
            public void onResponse(Call<Pholume> call, Response<Pholume> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    binder.updatePholume(response.body());
                    binder.updateLikes(footerBinding);
                    binder.updateLikeImage(footerBinding);
                } else {
                    Snackbar.make(binding.getRoot(),
                            response.code() + ": " + response.message(),
                            Snackbar.LENGTH_SHORT)
                            .show();
                    Log.e("like", response.message());
                }
            }
        };
        binder.bind(containerBinding, footerBinding, pholume, user, likeCallback);

        //set image listener
        containerBinding.pholumeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                        containerBinding.volumeImage.setImageDrawable(volumeOff);
                    } else {
                        containerBinding.volumeImage.setImageDrawable(volumeOn);
                        mediaPlayer = PholumeMediaPlayer.create(context,
                                Constants.BASE_AUDIO + pholume.audioUrl);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Error playing audio", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

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
}
