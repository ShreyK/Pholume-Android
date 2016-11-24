package android.pholume.com.pholume.Content.Capture;

import android.content.Context;
import android.os.Bundle;
import android.pholume.com.pholume.databinding.FragmentPholumeCaptureBinding;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.pholume.com.pholume.Content.Capture.CameraActivity.ReturnType.CAPTURE;

/**
 * PHOLUME CAMERA FRAGMENT
 * =======================
 * {@link OnFragmentInteractionListener} interface to handle events.
 */
public class PholumeCaptureFragment extends Fragment {

    private static final String LOG = PholumeCaptureFragment.class.getSimpleName();
    private static final String IMAGE_FILE = "IMAGE";
    private static final String AUDIO_FILE = "AUDIO";

    private OnFragmentInteractionListener mListener;
    private FragmentPholumeCaptureBinding mBinding;

    private String mImageFile;
    private String mAudioFile;
    private boolean mImageSaved;
    private boolean mAudioSaved;

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
        mImageSaved = false;
        mAudioSaved = false;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPholumeCaptureBinding.inflate(inflater, container, false);
        bindListeners();
        return mBinding.getRoot();
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

    /**
     * Called when Pholume has been captured and we must switch the view to the preview
     */
    private void onCaptured() {
        if (!(mImageSaved && mAudioSaved)) return;
        if (mListener != null) {
            if (TextUtils.isEmpty(mImageFile)) {
                Log.e(LOG, "Image File is empty/null");
            } else if (TextUtils.isEmpty(mAudioFile)) {
                Log.e(LOG, "Audio File is empty/null");
            } else {
                mListener.onFragmentInteraction(CAPTURE);
            }
        }
    }

    private void captureImage() {
        mImageSaved = true;
        onCaptured();
    }

    private void captureAudio() {
        mAudioSaved = true;
        onCaptured();
    }

    private void bindListeners() {
        mBinding.flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mBinding.rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        mBinding.capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
                captureAudio();
            }
        });
        mBinding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(CameraActivity.ReturnType.CLOSE);
            }
        });
    }
}
