package android.pholume.com.pholume.Content.Camera;

import android.content.Context;
import android.os.Bundle;
import android.pholume.com.pholume.databinding.FragmentPholumePreviewBinding;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * PHOLUME PREVIEW FRAGMENT
 * ========================
 * <p>
 * {@link PholumePreviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PholumePreviewFragment extends Fragment {

    private static final String LOG = PholumePreviewFragment.class.getSimpleName();
    private static final String IMAGE_FILE = "IMAGE";
    private static final String AUDIO_FILE = "AUDIO";

    public enum ReturnType {POST, CLOSE}

    private String mImageFile;
    private String mAudioFile;

    private OnFragmentInteractionListener mListener;
    private FragmentPholumePreviewBinding mBinding;

    public static PholumePreviewFragment newInstance(String imageFile, String audioFile) {
        PholumePreviewFragment fragment = new PholumePreviewFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_FILE, imageFile);
        args.putString(AUDIO_FILE, audioFile);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mImageFile = getArguments().getString(IMAGE_FILE);
            mAudioFile = getArguments().getString(AUDIO_FILE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mBinding = FragmentPholumePreviewBinding.inflate(inflater, container, false);
        bindViews();
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

    private void bindViews() {
        mBinding.pholumeCardContainer.pholumeFooter.setVisibility(View.GONE);
    }

    private void bindListeners() {
        mBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(ReturnType.CLOSE, null, null);
            }
        });

        mBinding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(ReturnType.POST, mImageFile, mAudioFile);
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
        void onFragmentInteraction(ReturnType type, String mImageFile, String mAudioFile);
    }
}
