package android.pholume.com.pholume.Model;


import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;

public class Pholume implements Parcelable {

    @SerializedName("_id")
    public String id;

    @SerializedName("uid")
    public String uid;

    @SerializedName("description")
    public String description;

    @SerializedName("date_created")
    public Date dateCreated;

    @SerializedName("last_updated")
    public Date lastUpdated;

    @SerializedName("photo")
    public String photoUrl;

    @SerializedName("audio")
    public String audioUrl;

    @SerializedName("width")
    public int width;

    @SerializedName("height")
    public int height;

    @SerializedName("likes")
    public int numComments;

    @SerializedName("likes")
    public HashSet<String> likes;

    Pholume(Parcel parcel) {
        Bundle bundle = parcel.readBundle(getClass().getClassLoader());
        this.id = bundle.getString("id");
        this.uid = bundle.getString("uid");
        this.description = bundle.getString("description");
        this.dateCreated = new Date(bundle.getLong("dateCreated"));
        this.lastUpdated = new Date(bundle.getLong("lastUpdated"));
        this.photoUrl = bundle.getString("photoUrl");
        this.audioUrl = bundle.getString("audioUrl");
        this.width = bundle.getInt("width");
        this.height = bundle.getInt("height");
        this.numComments = bundle.getInt("comments");
        this.likes = new HashSet<>(bundle.getStringArrayList("likes"));
    }

    public void print() {
        Log.d("PHOLUME", "ID: " + id + ", UID: " + uid);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("uid", uid);
        bundle.putString("description", description);
        bundle.putLong("dateCreated", dateCreated.getTime());
        bundle.putLong("lastUpdated", lastUpdated.getTime());
        bundle.putString("photoUrl", photoUrl);
        bundle.putString("audioUrl", audioUrl);
        bundle.putInt("width", width);
        bundle.putInt("height", height);
        bundle.putInt("comments", numComments);
        bundle.putStringArrayList("likes", new ArrayList<>(likes));
        parcel.writeBundle(bundle);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Pholume createFromParcel(Parcel in) {
            return new Pholume(in);
        }

        public Pholume[] newArray(int size) {
            return new Pholume[size];
        }
    };


    public String getNumberOfLikes() {
        if (likes == null) return "0";
        return String.valueOf(likes.size());
    }

    public String getNumberOfComments() {
        return String.valueOf(numComments);
    }

    public String getDate() {
        return DateFormat.getDateInstance().format(dateCreated);
    }
}
