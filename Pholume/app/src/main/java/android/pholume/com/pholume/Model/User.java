package android.pholume.com.pholume.Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashSet;

public class User implements Parcelable {
    @SerializedName("_id")
    public String id;
    @SerializedName("email")
    public String email;
    @SerializedName("username")
    public String username;
    @SerializedName("following")
    public HashSet<String> following;
    @SerializedName("followers")
    public HashSet<String> followers;
    @SerializedName("avatar")
    public String avatar;

    User(Parcel parcel) {
        Bundle bundle = parcel.readBundle(getClass().getClassLoader());
        this.id = bundle.getString("id");
        this.email = bundle.getString("email");
        this.username = bundle.getString("username");
        this.following = new HashSet<>(bundle.getStringArrayList("following"));
        this.followers = new HashSet<>(bundle.getStringArrayList("followers"));
        this.avatar = bundle.getString("avatar");
    }

    public void print() {
        Log.d("USER", "ID: " + id + ", EMAIL: " + email);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("id", id);
        bundle.putString("email", email);
        bundle.putString("username", username);
        bundle.putStringArrayList("following", new ArrayList<>(following));
        bundle.putStringArrayList("followers", new ArrayList<>(followers));
        bundle.putString("avatar", avatar);
        parcel.writeBundle(bundle);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getNumOfFollowers() {
        return String.valueOf(followers.size());
    }

    public String getNumOfFollowing() {
        return String.valueOf(following.size());
    }

    public boolean isFollowing(String uID) {
        if (following == null) following = new HashSet<>();
        return following.contains(uID);
    }

    public boolean hasFollower(String uID) {
        if (followers == null) followers = new HashSet<>();
        return followers.contains(uID);
    }

}
