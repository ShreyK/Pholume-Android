package android.pholume.com.pholume.Content.Capture;

import android.app.Activity;
import android.content.Context;
import android.media.MediaRecorder;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class PholumeRecorderManager {
    private final static int RECORD_TIME = 2000; //ms

    //Recording
    private Context mContext;
    private Activity mActivity;
    private PholumeCaptureFragment mFragment;
    private MediaRecorder mRecorder;
    private static File mAudioFile;

    PholumeRecorderManager(Activity activity, PholumeCaptureFragment fragment, String url) {
        mActivity = activity;
        mContext = activity;
        mFragment = fragment;
        mAudioFile = new File(url);
    }

    void startRecording() {
        if (mRecorder != null) return;
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mRecorder.setOutputFile(mAudioFile.getAbsolutePath());
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mRecorder.setMaxDuration(RECORD_TIME);
        mRecorder.setOnInfoListener(new MediaRecorder.OnInfoListener() {
            @Override
            public void onInfo(MediaRecorder mediaRecorder, int i, int i1) {
                if (i == MediaRecorder.MEDIA_RECORDER_INFO_MAX_DURATION_REACHED) {
                    mFragment.setProgressBarVisibile(View.GONE);
                    mRecorder.stop();
                    mRecorder.release();
                    mRecorder = null;
                    PholumeCaptureFragment.mAudioSaved = true;
                    PholumeCaptureFragment.onCaptured();
                }
            }
        });
        try {
            mRecorder.prepare();
            mFragment.setProgressBarVisibile(View.VISIBLE);
            mRecorder.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showToast(final String text) {
        if (mActivity != null) {
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(mActivity, text, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
