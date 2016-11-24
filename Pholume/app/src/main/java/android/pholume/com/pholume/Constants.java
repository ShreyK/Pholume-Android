package android.pholume.com.pholume;

import java.io.File;

public class Constants {

    public static final String BASE_URL = "http://35.162.237.150:8080/";
    public static final String API_URL = BASE_URL + "api/";
    public static final String BASE_UPLOADS = BASE_URL + "public/uploads/";
    public static final String BASE_AUDIO = BASE_UPLOADS + "audio/";
    public static final String BASE_PHOTO = BASE_UPLOADS + "photo/";
    public static final String BASE_AVATAR = BASE_UPLOADS + "avatar/";
    public static final String BASE_PUBLIC_FOLDER = "Pholume";
    public static final String PUBLIC_IMAGE_FOLDER = BASE_PUBLIC_FOLDER + File.separator + "Images";
    public static final String PUBLIC_AUDIO_FOLDER = BASE_PUBLIC_FOLDER + File.separator +  "Audio";
}
