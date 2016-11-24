package android.pholume.com.pholume.Content.Capture;

import android.os.Bundle;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.Util;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.util.Date;

public class CameraActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    public enum ReturnType {CAPTURE, POST, CLOSE}

    private String mImageFile;
    private String mAudioFile;

    private PholumeCaptureFragment captureFragment;
    private PholumePreviewFragment previewFragment;

    @Override
    public void onFragmentInteraction(ReturnType type) {
        switch (type) {
            case CAPTURE:
                setPreviewFragment();
                break;
            case POST:
                postPholume();
                break;
            case CLOSE:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        Date date = new Date();
        mImageFile = Util.getRootImageDir() + date + ".jpg";
        mAudioFile = Util.getRootImageDir() + date + ".mp3";

        setCaptureFragment();
    }

    private void postPholume() {
        finish();
    }

    private void setCaptureFragment() {
        if (captureFragment == null) {
            captureFragment = PholumeCaptureFragment.newInstance(mImageFile, mAudioFile);
        }
        setFragment(captureFragment);
    }

    private void setPreviewFragment() {
        if (previewFragment == null) {
            previewFragment = PholumePreviewFragment.newInstance(mImageFile, mAudioFile);
        }
        setFragment(previewFragment);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
