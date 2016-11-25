package android.pholume.com.pholume;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import static android.os.Environment.getExternalStorageDirectory;

public class Util {
    /**
     * Check if this device has a camera
     */
    public static boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    public static boolean getAudioFocus(Context context, AudioManager.OnAudioFocusChangeListener listener) {
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
        long duration = Calendar.getInstance().getTimeInMillis() - dateCreated.getTime();

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

    public static void setupRootDirs() {
        if (isExternalStorageWritable()) {
            String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + Constants.BASE_PUBLIC_FOLDER;
            String rootImagePath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + Constants.PUBLIC_IMAGE_FOLDER;
            String rootAudioPath = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + File.separator + Constants.PUBLIC_AUDIO_FOLDER;
            File rootDir = new File(rootPath);
            File rootImageDir = new File(rootImagePath);
            File rootAudioDir = new File(rootAudioPath);
            if (!rootDir.exists()) {
                rootDir.mkdir();
            }
            if (!rootImageDir.exists()) {
                rootImageDir.mkdir();
            }
            if (!rootAudioDir.exists()) {
                rootAudioDir.mkdir();
            }
        } else {
            System.err.println("CANNOT WRITE TO EXTERNAL STORAGE");
        }
    }

    public static String getRootImageDir() {
        // Get the directory for the user's public pictures directory.
        return getExternalStorageDirectory().getAbsolutePath()
                + File.separator + Constants.PUBLIC_IMAGE_FOLDER;
    }

    public static String getRootAudioDir() {
        // Get the directory for the user's public pictures directory.
        return getExternalStorageDirectory().getAbsolutePath()
                + File.separator + Constants.PUBLIC_IMAGE_FOLDER;
    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }
}
