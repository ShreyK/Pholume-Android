package android.pholume.com.pholume.Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;


public class Comment implements Parcelable {

    @SerializedName("_id")
    public String id;

    @SerializedName("uid")
    public String uid;

    @SerializedName("pid")
    public String pid;

    @SerializedName("date_created")
    public String dateCreated;

    @SerializedName("comment")
    public String comment;

    Comment(Parcel parcel) {
        Bundle bundle = parcel.readBundle();
        this.id = bundle.getString("id");
        this.uid = bundle.getString("uid");
        this.pid = bundle.getString("pid");
        this.dateCreated = bundle.getString("dateCreated");
        this.comment = bundle.getString("comment");
    }

    public void print() {
        Log.d("COMMMENT", "ID: " + id + ", UID: " + uid + ", PID: " + pid);
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
        bundle.putString("pid", pid);
        bundle.putString("dateCreated", dateCreated);
        bundle.putString("comment", comment);
        parcel.writeBundle(bundle);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Comment createFromParcel(Parcel in) {
            return new Comment(in);
        }

        public Comment[] newArray(int size) {
            return new Comment[size];
        }
    };
}
