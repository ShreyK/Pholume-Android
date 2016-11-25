package android.pholume.com.pholume.Content.Capture;

import android.os.Bundle;
import android.pholume.com.pholume.R;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CaptureActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    public enum ReturnType {CAPTURE, POST, CLOSE}

    private String mImageFile;
    private String mAudioFile;

    private PholumeCaptureFragment captureFragment;
    private PholumePreviewFragment previewFragment;

    @Override
    public void onFragmentInteraction(ReturnType type) {
        switch (type) {
            case CAPTURE:
                setPreviewFragment(captureFragment.getImageWidth(),captureFragment.getImageHeight());
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

        SimpleDateFormat format = new SimpleDateFormat("d-M-yy-H:m:s");
        Date date = new Date();
        DateFormat.getTimeInstance();

        String imageFileName = format.format(date) + ".jpg";
        String audioFileName = format.format(date) + ".mp3";

        try {
            File imageFile = new File(getExternalFilesDir(null), imageFileName);
            mImageFile = imageFile.getPath();
            File audioFile = new File(getExternalFilesDir(null), audioFileName);
            mAudioFile = audioFile.getPath();
        } catch (Exception e) {
            System.err.println(mImageFile);
            System.err.println(mAudioFile);
            e.printStackTrace();
        }

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

    private void setPreviewFragment(int width, int height) {
        if (previewFragment == null) {
            previewFragment = PholumePreviewFragment.newInstance(mImageFile, mAudioFile, width, height);
        }
        setFragment(previewFragment);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
