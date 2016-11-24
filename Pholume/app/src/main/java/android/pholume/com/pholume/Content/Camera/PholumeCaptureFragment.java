package android.pholume.com.pholume.Content.Camera;

import android.content.Context;
import android.os.Bundle;
import android.pholume.com.pholume.databinding.FragmentPholumeCaptureBinding;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * PHOLUME CAMERA FRAGMENT
 * =======================
 * {@link PholumeCaptureFragment.OnFragmentInteractionListener} interface to handle events.
 */
public class PholumeCaptureFragment extends Fragment {

    private static final String LOG = PholumeCaptureFragment.class.getSimpleName();

    public enum ReturnType {CAPTURE, CLOSE}

    ;

    private OnFragmentInteractionListener mListener;
    private FragmentPholumeCaptureBinding mBinding;

    private String mImageFile;
    private String mAudioFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        if (mListener != null) {
            if (TextUtils.isEmpty(mImageFile)) {
                Log.e(LOG, "Image File is empty/null");
            } else if (TextUtils.isEmpty(mAudioFile)) {
                Log.e(LOG, "Audio File is empty/null");
            } else {
                mListener.onFragmentInteraction(ReturnType.CAPTURE, mImageFile, mAudioFile);
            }
        }
    }

    private void captureImage() {

    }

    private void captureAudio() {

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
                mListener.onFragmentInteraction(ReturnType.CLOSE, null, null);
            }
        });
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(ReturnType type, String image, String audio);
    }
}
