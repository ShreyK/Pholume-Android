package android.pholume.com.pholume.Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

public class FeedItem implements Parcelable{

    @SerializedName("user")
    public User user;

    @SerializedName("pholume")
    public Pholume pholume;

    FeedItem(Parcel parcel) {
        Bundle bundle = parcel.readBundle();
        this.user = bundle.getParcelable("user");
        this.pholume = bundle.getParcelable("pholume");
    }

    public void print(){
        Log.d("FEED ITEM", "User: " + user.id + ", Pholume: " + pholume.id);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putParcelable("pholume", pholume);
        parcel.writeBundle(bundle);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public FeedItem createFromParcel(Parcel in) {
            return new FeedItem(in);
        }

        public FeedItem[] newArray(int size) {
            return new FeedItem[size];
        }
    };
}
