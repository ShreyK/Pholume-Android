package android.pholume.com.pholume.Content.Capture;

import android.content.Context;
import android.os.Bundle;
import android.pholume.com.pholume.Content.Common.PholumeBinder;
import android.pholume.com.pholume.Model.CapturedPholume;
import android.pholume.com.pholume.PholumeMediaPlayer;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.FragmentPholumePreviewBinding;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.FileInputStream;

import static android.pholume.com.pholume.Content.Capture.CaptureActivity.ReturnType.CLOSE;
import static android.pholume.com.pholume.Content.Capture.CaptureActivity.ReturnType.POST;

/**
 * PHOLUME PREVIEW FRAGMENT
 * ========================
 * <p>
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class PholumePreviewFragment extends Fragment {

    private static final String LOG = PholumePreviewFragment.class.getSimpleName();
    private static final String IMAGE_FILE = "IMAGE";
    private static final String AUDIO_FILE = "AUDIO";
    private static final String CAPTURED_PHOLUME = "CAPTURED_PHOLUME";
    private static final String SAVED_DESCRIPTION = "SAVED_DESCRIPTION";

    private Context mContext;
    private String mImageFile;
    private String mAudioFile;
    private static CapturedPholume mPholume;

    private OnFragmentInteractionListener mListener;
    private FragmentPholumePreviewBinding mBinding;
    private PholumeMediaPlayer mMediaPlayer;
    private PholumeBinder mBinder;

    public static PholumePreviewFragment newInstance(String imageFile, String audioFile, CapturedPholume pholume) {
        PholumePreviewFragment fragment = new PholumePreviewFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_FILE, imageFile);
        args.putString(AUDIO_FILE, audioFile);
        args.putParcelable(CAPTURED_PHOLUME, pholume);
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
            mPholume = args.getParcelable(CAPTURED_PHOLUME);
        }
        mContext = getContext();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mBinding == null) return;
        if (outState == null) {
            outState = new Bundle();
        }
        outState.putString(SAVED_DESCRIPTION, mBinding.title.getText().toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mBinding = FragmentPholumePreviewBinding.inflate(inflater, container, false);
        if (bundle != null && !bundle.isEmpty()) {
            mBinding.title.setText(bundle.getString(SAVED_DESCRIPTION));
        }
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
        if (mBinder == null) mBinder = new PholumeBinder(getActivity());
        mBinder.bind(mBinding.image, mBinding.title, mPholume);
    }

    private void bindListeners() {
        mBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(CLOSE, null);
            }
        });

        mBinding.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    FileInputStream is = new FileInputStream(mAudioFile);
                    if (mMediaPlayer == null) {
                        mMediaPlayer = PholumeMediaPlayer.create(mContext, is.getFD());
                        mBinding.volumeImage.setImageDrawable(
                                getContext().getDrawable(R.drawable.ic_volume_on));
                    } else if (mMediaPlayer.isPlaying()) {
                        mMediaPlayer.stop();
                        mMediaPlayer = null;
                        mBinding.volumeImage.setImageDrawable(
                                getContext().getDrawable(R.drawable.ic_volume_off));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mBinding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String title = mBinding.title.getText().toString();
                if (TextUtils.isEmpty(title)) {
                    mBinding.title.setError("Required");
                } else {
                    mPholume.setDescription(title);
                    mListener.onFragmentInteraction(POST, mPholume);
                }
            }
        });
    }
}
