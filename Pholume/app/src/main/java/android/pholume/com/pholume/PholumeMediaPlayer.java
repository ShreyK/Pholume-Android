package android.pholume.com.pholume;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import java.io.FileDescriptor;
import java.io.IOException;

public class PholumeMediaPlayer {
    private static final String TAG = PholumeMediaPlayer.class.getSimpleName();

    private Context mContext = null;
    private FileDescriptor mFileDescriptor = null;
    private Uri mUri = null;
    private int mCounter = 1;

    private MediaPlayer mCurrentPlayer = null;
    private MediaPlayer mNextPlayer = null;

    public static PholumeMediaPlayer create(Context context, String url) {
        return new PholumeMediaPlayer(context, url);
    }

    public static PholumeMediaPlayer create(Context context, FileDescriptor url) {
        return new PholumeMediaPlayer(context, url);
    }

    private PholumeMediaPlayer(Context context, String url) {
        mContext = context;
        mUri = Uri.parse(url);

        mCurrentPlayer = MediaPlayer.create(mContext, mUri);
        mCurrentPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mCurrentPlayer.start();
            }
        });
        setNextPlayer();
    }

    private PholumeMediaPlayer(Context context, FileDescriptor fileDescriptor) {
        mContext = context;
        mFileDescriptor = fileDescriptor;
        try {
            mCurrentPlayer = new MediaPlayer();
            mCurrentPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mCurrentPlayer.setDataSource(fileDescriptor);
            mCurrentPlayer.prepareAsync();
            mCurrentPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    mCurrentPlayer.start();
                }
            });
            setNextPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setNextPlayer() {
        try {
            if (mFileDescriptor != null) {
                mNextPlayer = new MediaPlayer();
                mNextPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mNextPlayer.setDataSource(mFileDescriptor);
                mNextPlayer.prepareAsync();
                mNextPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        mCurrentPlayer.setNextMediaPlayer(mNextPlayer);
                    }
                });
            } else {
                mNextPlayer = MediaPlayer.create(mContext, mUri);
                mCurrentPlayer.setNextMediaPlayer(mNextPlayer);
            }
            mCurrentPlayer.setOnCompletionListener(onCompletionListener);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            mediaPlayer.release();
            mCurrentPlayer = mNextPlayer;
            setNextPlayer();
            Log.d(TAG, String.format("Loop #%d", ++mCounter));
        }
    };

    // code-read additions:
    public boolean isPlaying() throws IllegalStateException {
        return mCurrentPlayer.isPlaying();
    }

    public void setVolume(float leftVolume, float rightVolume) {
        mCurrentPlayer.setVolume(leftVolume, rightVolume);
        mNextPlayer.setVolume(leftVolume, rightVolume);
    }

    public void start() throws IllegalStateException {
        mCurrentPlayer.start();
    }

    public void stop() throws IllegalStateException {
        if (mCurrentPlayer != null && mCurrentPlayer.isPlaying()) {
            mCurrentPlayer.stop();
        }
        reset();
    }

    public void destroy() throws IllegalStateException {
        if (mCurrentPlayer != null && mCurrentPlayer.isPlaying()) {
            mCurrentPlayer.stop();
        }
        release();
    }

    public void pause() throws IllegalStateException {
        mCurrentPlayer.pause();
    }

    public void release() {
        mCurrentPlayer.release();
        mNextPlayer.release();
    }

    public void reset() {
        mCurrentPlayer.reset();
        mNextPlayer.reset();
    }
}
