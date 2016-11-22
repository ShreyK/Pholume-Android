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
    public String email;
    public String username;
    public HashSet<String> following;
    public HashSet<String> followers;
    public String avatar;

    User(String id, String email, String username, HashSet<String> following, HashSet<String> followers, String avatar) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.avatar = avatar;
        if (following == null) {
            this.following = new HashSet<>();
        } else {
            this.following = following;
        }
        if (followers == null) {
            this.followers = new HashSet<>();
        } else {
            this.followers = followers;
        }
    }

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

    public String getNumOfFollowers(){
        return String.valueOf(followers.size());
    }
    public String getNumOfFollowing(){
        return String.valueOf(following.size());
    }

    public boolean isFollowing(String uID){
        return following.contains(uID);
    }

    public boolean hasFollower(String uID){
        return followers.contains(uID);
    }

}
