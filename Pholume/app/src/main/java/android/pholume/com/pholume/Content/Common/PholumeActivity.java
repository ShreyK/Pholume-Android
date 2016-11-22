package android.pholume.com.pholume.Content.Common;

import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.ActivityPholumeBinding;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class PholumeActivity extends AppCompatActivity {

    private static MediaPlayer mediaPlayer;
    private Drawable volumeOff;
    private Drawable volumeOn;

    ActivityPholumeBinding binding;
    PholumeBinder binder;
    PholumeCallback<Pholume> likeCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_pholume);
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
                    binder.updateLikes();
                    binder.updateLikeImage();
                } else {
                    Snackbar.make(binding.pholumeCardContainer.getRoot(),
                            response.code() + ": " + response.message(),
                            Snackbar.LENGTH_SHORT)
                            .show();
                    Log.e("like", response.message());
                }
            }
        };
        binder.bind(binding.pholumeCardContainer, pholume, user, likeCallback);

        //set image listener
        binding.pholumeCardContainer.pholumeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(mediaPlayer != null && mediaPlayer.isPlaying()){
                        mediaPlayer.stop();
                        initMediaPlayer();
                        binding.pholumeCardContainer.volumeImage.setImageDrawable(volumeOff);
                    } else {
                        binding.pholumeCardContainer.volumeImage.setImageDrawable(volumeOn);
                        playAudio(Constants.BASE_AUDIO + pholume.audioUrl);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Snackbar.make(view, "Couldnt find Pholume audio :(", Snackbar.LENGTH_SHORT)
                            .show();
                }
            }
        });
    }

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

    protected static void initMediaPlayer() {
        if (mediaPlayer == null) mediaPlayer = new MediaPlayer();
        mediaPlayer.reset();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setLooping(true);
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
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

    private static void playAudio(String url) throws IOException {
        if (mediaPlayer == null) {
            return;
        }
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
        initMediaPlayer();
        mediaPlayer.setDataSource(url);
        mediaPlayer.prepareAsync();
    }
}
