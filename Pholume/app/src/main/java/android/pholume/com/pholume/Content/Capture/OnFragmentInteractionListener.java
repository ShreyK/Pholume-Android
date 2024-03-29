package android.pholume.com.pholume.Content.Capture;

import android.pholume.com.pholume.Model.CapturedPholume;

/**
 * This interface must be implemented by activities that contain this
 * fragment to allow an interaction in this fragment to be communicated
 * to the activity and potentially other fragments contained in that
 * activity.
 */
public interface OnFragmentInteractionListener {
    void onFragmentInteraction(CaptureActivity.ReturnType type, CapturedPholume pholume);
}
