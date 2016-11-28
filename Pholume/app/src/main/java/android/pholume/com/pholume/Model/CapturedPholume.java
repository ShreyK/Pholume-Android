package android.pholume.com.pholume.Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.pholume.com.pholume.PrefManager;
import android.util.Log;

import static android.R.attr.id;

public class CapturedPholume implements Parcelable {

    private User user;
    public String description;
    public String photoUrl;
    public String audioUrl;
    public int width;
    public int height;

    public CapturedPholume(String photoUrl, String audioUrl) {
        this.user = PrefManager.getInstance().getCurrentUser();
        this.description = "";
        this.photoUrl = photoUrl;
        this.audioUrl = audioUrl;
        this.width = 0;
        this.height = 0;
    }

    public CapturedPholume(String desc, String photoUrl, String audioUrl, int width, int height) {
        this.user = PrefManager.getInstance().getCurrentUser();
        this.description = desc;
        this.photoUrl = photoUrl;
        this.audioUrl = audioUrl;
        this.width = width;
        this.height = height;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void setDescription(String title) {
        this.description = title;
    }

    CapturedPholume(Parcel parcel) {
        Bundle bundle = parcel.readBundle(getClass().getClassLoader());
        this.description = bundle.getString("description");
        this.photoUrl = bundle.getString("photoUrl");
        this.audioUrl = bundle.getString("audioUrl");
        this.width = bundle.getInt("width");
        this.height = bundle.getInt("height");
    }

    public void print() {
        Log.d("PHOLUME", "ID: " + id + ", photo: " + photoUrl + ", audio" + audioUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("description", description);
        bundle.putString("photoUrl", photoUrl);
        bundle.putString("audioUrl", audioUrl);
        bundle.putInt("width", width);
        bundle.putInt("height", height);
        parcel.writeBundle(bundle);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CapturedPholume createFromParcel(Parcel in) {
            return new CapturedPholume(in);
        }

        public CapturedPholume[] newArray(int size) {
            return new CapturedPholume[size];
        }
    };
}
