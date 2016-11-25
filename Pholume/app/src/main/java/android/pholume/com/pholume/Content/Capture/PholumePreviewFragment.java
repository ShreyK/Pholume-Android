package android.pholume.com.pholume.Content.Capture;

import android.content.Context;
import android.os.Bundle;
import android.pholume.com.pholume.Content.Common.PholumeBinder;
import android.pholume.com.pholume.databinding.FragmentPholumePreviewBinding;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
    private static final String IMAGE_WIDTH = "IMAGE_WIDTH";
    private static final String IMAGE_HEIGHT = "IMAGE_HEIGHT";
    private static final String AUDIO_FILE = "AUDIO";
    private static final String SAVED_DESCRIPTION = "SAVED_DESCRIPTION";

    private String mImageFile;
    private String mAudioFile;

    private int mImageWidth;
    private int mImageHeight;

    private OnFragmentInteractionListener mListener;
    private FragmentPholumePreviewBinding mBinding;
    private PholumeBinder mBinder;

    public static PholumePreviewFragment newInstance(String imageFile, String audioFile, int w, int h) {
        PholumePreviewFragment fragment = new PholumePreviewFragment();
        Bundle args = new Bundle();
        args.putString(IMAGE_FILE, imageFile);
        args.putString(AUDIO_FILE, audioFile);
        args.putInt(IMAGE_WIDTH, w);
        args.putInt(IMAGE_HEIGHT, h);
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
            mImageWidth = args.getInt(IMAGE_WIDTH);
            mImageHeight = args.getInt(IMAGE_HEIGHT);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mBinding == null) return;
        if (outState == null) {
            outState = new Bundle();
        }
        outState.putString(SAVED_DESCRIPTION, mBinding.toString());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        mBinding = FragmentPholumePreviewBinding.inflate(inflater, container, false);
        if (bundle != null && !bundle.isEmpty()) {
            mBinding.pholumeCardContainer.pholumeTitle.setText(bundle.getString(SAVED_DESCRIPTION));
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
        if(mBinder == null) mBinder = new PholumeBinder(getContext());
        mBinder.bind(mBinding.pholumeCardContainer, mImageFile, mAudioFile, mImageWidth, mImageHeight);
    }

    private void bindListeners() {
        mBinding.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onFragmentInteraction(CLOSE);
            }
        });

        mBinding.post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String desc = mBinding.pholumeCardContainer.pholumeTitle.getText().toString();
                if (TextUtils.isEmpty(desc)) {
                    mBinding.pholumeCardContainer.pholumeTitle.setError("Required");
                } else {
                    mListener.onFragmentInteraction(POST);
                }
            }
        });
    }

//
//    private void postPholume(String desc) {
//        RequestBody pFile = RequestBody.create(MediaType.parse("multipart/form-data"), mImageFile);
//        MultipartBody.Part photoBody =
//                MultipartBody.Part.createFormData("photo", mImageFile.getName(), pFile);
//        RequestBody aFile = RequestBody.create(MediaType.parse("multipart/form-data"), mAudioFile);
//        MultipartBody.Part audioBody =
//                MultipartBody.Part.createFormData("audio", mAudioFile.getName(), aFile);
//        RequestBody description = RequestBody.create(MediaType.parse("multipart/form-data"), desc);
//        RestManager.getInstance().postPholume(photoBody, audioBody, description, new PholumeCallback<Pholume>("PostPholume") {
//            @Override
//            public void onResponse(Call<Pholume> call, Response<Pholume> response) {
//                super.onResponse(call, response);
//                if (response.isSuccessful()) {
//                    audioSaved = false;
//                    imageSaved = false;
//                    Log.d("UPLOAD SUCCESSFUL", response.body().id);
//                    getActivity().finish();
//                }
//            }
//        });
//    }

}
