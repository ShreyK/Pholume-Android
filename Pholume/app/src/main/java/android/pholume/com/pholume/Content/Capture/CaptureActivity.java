package android.pholume.com.pholume.Content.Capture;

import android.os.Bundle;
import android.pholume.com.pholume.R;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class CaptureActivity extends AppCompatActivity implements OnFragmentInteractionListener {

    public enum ReturnType {CAPTURE, POST, CLOSE}

    private String mImageFileName;
    private String mAudioFileName;
    private File mImageFile;
    private File mAudioFile;

    private PholumeCaptureFragment captureFragment;
    private PholumePreviewFragment previewFragment;

    @Override
    public void onFragmentInteraction(ReturnType type) {
        switch (type) {
            case CAPTURE:
                setPreviewFragment();
                break;
            case POST:
                postPholume(previewFragment.getTitle());
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
            mImageFile = new File(getExternalFilesDir(null), imageFileName);
            mImageFileName = mImageFile.getPath();
            mAudioFile = new File(getExternalFilesDir(null), audioFileName);
            mAudioFileName = mAudioFile.getPath();
        } catch (Exception e) {
            System.err.println(mImageFileName);
            System.err.println(mAudioFileName);
            e.printStackTrace();
        }

        setCaptureFragment();
    }

    private void setCaptureFragment() {
        if (captureFragment == null) {
            captureFragment = PholumeCaptureFragment.newInstance(mImageFileName, mAudioFileName);
        }
        setFragment(captureFragment);
    }

    private void setPreviewFragment() {
        if (previewFragment == null) {
            previewFragment = PholumePreviewFragment.newInstance(mImageFileName, mAudioFileName);
        }
        setFragment(previewFragment);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }


    private void postPholume(String desc) {
        RequestBody pFile = RequestBody.create(MediaType.parse("multipart/form-data"), mImageFileName);
        MultipartBody.Part photoBody =
                MultipartBody.Part.createFormData("photo", mImageFile.getName(), pFile);
        RequestBody aFile = RequestBody.create(MediaType.parse("multipart/form-data"), mAudioFileName);
        MultipartBody.Part audioBody =
                MultipartBody.Part.createFormData("audio", mAudioFile.getName(), aFile);
        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), desc);
        System.out.println("========");
        System.out.println("photo " + mImageFile.getName() + " " + pFile.toString());
        System.out.println("audio" + mAudioFile.getName() + " " + aFile.toString());
        System.out.println("desc " + desc);
        System.out.println("========");
//        RestManager.getInstance().postPholume(photoBody, audioBody, description, new PholumeCallback<Pholume>("PostPholume") {
//            @Override
//            public void onResponse(Call<Pholume> call, Response<Pholume> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    Log.d("UPLOAD SUCCESSFUL", response.body().id);
//                    finish();
//                }
//            }
//        });
    }
}
