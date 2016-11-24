package android.pholume.com.pholume.Model;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class CommentItem implements Parcelable {

    public User user;

    public Comment comment;

    CommentItem(Parcel parcel) {
        Bundle bundle = parcel.readBundle(getClass().getClassLoader());
        this.user = bundle.getParcelable("user");
        this.comment = bundle.getParcelable("comment");
    }

    public void print() {
        Log.d("COMMENT ITEM", "User: " + user.id + ", Comment: " + comment.comment);
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", user);
        bundle.putParcelable("comment", comment);
        parcel.writeBundle(bundle);
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CommentItem createFromParcel(Parcel in) {
            return new CommentItem(in);
        }

        public CommentItem[] newArray(int size) {
            return new CommentItem[size];
        }
    };
}
