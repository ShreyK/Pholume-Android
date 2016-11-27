package android.pholume.com.pholume.Content.Capture;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.FragmentPholumeCaptureBinding;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.pholume.com.pholume.Content.Capture.PholumeCameraManager.FlashType.AUTO;

/**
 * PHOLUME CAMERA FRAGMENT
 * =======================
 * {@link OnFragmentInteractionListener} interface to handle events.
 */
public class PholumeCaptureFragment extends Fragment implements View.OnClickListener {

    private static final String LOG = PholumeCaptureFragment.class.getSimpleName();
    private static final String IMAGE_FILE = "IMAGE";
    private static final String AUDIO_FILE = "AUDIO";

    private Context mContext;
    private Activity mActivity;
    private static OnFragmentInteractionListener mListener;
    private FragmentPholumeCaptureBinding mBinding;
    private PholumeCameraManager mPholumeCameraManager;
    private PholumeRecorderManager mPholumeRecorderManager;

    private static String mImageFile;
    private static String mAudioFile;
    public static boolean mImageSaved;
    public static boolean mAudioSaved;
    public AutoFitTextureView mTextureView;

    //Views
    private static Drawable flashAutoDrawable;
    private static Drawable flashOnDrawable;
    private static Drawable flashOffDrawable;

    public static PholumeCaptureFragment newInstance(String imageFile, String audioFile) {
        PholumeCaptureFragment fragment = new PholumeCaptureFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_FILE, imageFile);
        args.putString(AUDIO_FILE, audioFile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            mImageFile = args.getString(IMAGE_FILE);
            mAudioFile = args.getString(AUDIO_FILE);
        }

        mActivity = getActivity();
        mContext = getContext();
        System.out.println("SETTING PCM");
        if (mPholumeCameraManager == null) {
            mPholumeCameraManager = new PholumeCameraManager(mActivity, this, mImageFile);
        }
        if (mPholumeRecorderManager == null) {
            mPholumeRecorderManager = new PholumeRecorderManager(mActivity, this, mAudioFile);
        }
        mImageSaved = false;
        mAudioSaved = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPholumeCaptureBinding.inflate(inflater, container, false);
        mTextureView = mBinding.texture;
        flashAutoDrawable = mContext.getDrawable(R.drawable.ic_flash_auto);
        flashOnDrawable = mContext.getDrawable(R.drawable.ic_flash_on);
        flashOffDrawable = mContext.getDrawable(R.drawable.ic_flash_off);

        bindViews();
        return mBinding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPholumeCameraManager.startBackgroundThread();

        // When the screen is turned off and turned back on, the SurfaceTexture is already
        // available, and "onSurfaceTextureAvailable" will not be called. In that case, we can open
        // a camera and start preview from here (otherwise, we wait until the surface is ready in
        // the SurfaceTextureListener).
        if (mBinding.texture.isAvailable()) {
            mPholumeCameraManager.openCamera(mBinding.texture.getWidth(), mBinding.texture.getHeight());
        } else {
            mBinding.texture.setSurfaceTextureListener(mPholumeCameraManager.mSurfaceTextureListener);
        }
    }

    @Override
    public void onPause() {
        mPholumeCameraManager.closeCamera();
        mPholumeCameraManager.stopBackgroundThread();
        super.onPause();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.capture:
                setProgressBarVisibile(View.VISIBLE);
                mPholumeCameraManager.takePicture();
                mPholumeRecorderManager.startRecording();
                break;
            case R.id.flash:
                switch (mPholumeCameraManager.getFlashType()) {
                    case AUTO:
                        mPholumeCameraManager.setFlashType(PholumeCameraManager.FlashType.YES);
                        break;
                    case YES:
                        mPholumeCameraManager.setFlashType(PholumeCameraManager.FlashType.NO);
                        break;
                    case NO:
                        mPholumeCameraManager.setFlashType(AUTO);
                        break;
                }
                updateFlashView();
                break;
            case R.id.rotate:
                mPholumeCameraManager.setIsFacingUser();
                mPholumeCameraManager.openCamera(mTextureView.getWidth(), mTextureView.getHeight());
                break;
            case R.id.close:
                mListener.onFragmentInteraction(CaptureActivity.ReturnType.CLOSE);
                break;
        }
    }

    /**
     * Called when Pholume has been captured and we must switch the view to the preview
     */
    public static void onCaptured() {
        if (!(mImageSaved && mAudioSaved)) return;
        if (mListener != null) {
            if (TextUtils.isEmpty(mImageFile)) {
                Log.e(LOG, "Image File is empty/null");
            } else if (TextUtils.isEmpty(mAudioFile)) {
                Log.e(LOG, "Audio File is empty/null");
            } else {
                mListener.onFragmentInteraction(CaptureActivity.ReturnType.CAPTURE);
            }
        }
    }

    private void bindViews() {
        mBinding.capture.setOnClickListener(this);
        mBinding.flash.setOnClickListener(this);
        mBinding.rotate.setOnClickListener(this);
        mBinding.close.setOnClickListener(this);

        updateFlashView();
    }

    private void updateFlashView() {
        switch (mPholumeCameraManager.getFlashType()) {
            case AUTO:
                mBinding.flash.setImageDrawable(flashAutoDrawable);
                break;
            case YES:
                mBinding.flash.setImageDrawable(flashOnDrawable);
                break;
            case NO:
                mBinding.flash.setImageDrawable(flashOffDrawable);
                break;
        }
    }

    public void setProgressBarVisibile(final int visibile) {
//        mActivity.runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
        mBinding.progressBar.setVisibility(visibile);
//            }
//        });
    }
}
