package android.pholume.com.pholume;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.webkit.MimeTypeMap;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Util {

    /** Check if this device has a camera */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static boolean getAudioFocus(Context context, AudioManager.OnAudioFocusChangeListener listener){
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int result = audioManager.requestAudioFocus(listener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN);

        if (result != AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            return false;
        }
        return true;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getTimeSince(Date dateCreated, Context context) {
        long duration  = Calendar.getInstance().getTimeInMillis() - dateCreated.getTime();

        int diffInSeconds = (int) TimeUnit.MILLISECONDS.toSeconds(duration);
        int diffInMinutes = (int) TimeUnit.MILLISECONDS.toMinutes(duration);
        int diffInHours = (int) TimeUnit.MILLISECONDS.toHours(duration);
        int diffInDays = (int) TimeUnit.MILLISECONDS.toDays(duration);

        if (diffInDays >= 7) { // Weeks
            if (diffInDays > 30) { // Months
                return context.getResources().getQuantityString(R.plurals.months_ago, diffInDays / 30, diffInDays / 30);
            } else {
                return context.getResources().getQuantityString(R.plurals.weeks_ago, diffInDays / 7, diffInDays / 7);
            }
        } else if (diffInDays > 0) { // Days
            return context.getResources().getQuantityString(R.plurals.days_ago, diffInDays, diffInDays);
        } else { // less than a day
            if (diffInHours > 0) { // Hours
                return context.getResources().getQuantityString(R.plurals.hours_ago, diffInHours, diffInHours);
            } else {
                if (diffInMinutes > 0) {
                    return context.getResources().getQuantityString(R.plurals.minutes_ago, diffInMinutes, diffInMinutes);
                } else {
                    return context.getResources().getQuantityString(R.plurals.seconds_ago, diffInSeconds, diffInSeconds);
                }
            }
        }
    }
}
