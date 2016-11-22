package android.pholume.com.pholume.Network;


import android.pholume.com.pholume.Model.CommentItem;
import android.pholume.com.pholume.Model.FeedItem;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface RestService {

    @FormUrlEncoded
    @POST("auth/register")
        // or login
    Call<User> login(@Field("email") String username, @Field("password") String password);

    @GET("auth/logout")
    Call<Void> logout();

    @GET("pholume")
    Call<List<Pholume>>  getProfile();

    @GET("user/me")
    Call<User> getCurrentUser();

    @GET("user/{id}")
    Call<User> getUser(@Path("id") String uid);

    @GET("user/{uid}/pholumes")
    Call<List<Pholume>> getPholumes(@Path("uid") String uid);

    @GET("feed")
    Call<List<FeedItem>> getFeed();

    @Multipart
    @POST("pholume")
    Call<Pholume> postPholume (@Part MultipartBody.Part photoFile,
                               @Part MultipartBody.Part audioFile,
                               @Part("description") RequestBody description);

    @POST("pholume/like/{id}")
    Call<Pholume> postLike(@Path("id") String id);

    @POST("user/search/{username}")
    Call<List<User>> searchForUser(@Path("username") String username);

    @GET("user/{id}/following")
    Call<List<User>> getFollowing(@Path("id") String id);

    @GET("user/{id}/followers")
    Call<List<User>> getFollowers(@Path("id") String id);

    @POST("user/follow/{id}")
    Call<User> postFollow(@Path("id") String uid);

    @POST("comment/{id}")
    Call<List<CommentItem>> getComments(@Path("id") String pid);

    @FormUrlEncoded
    @POST("comment")
    Call<Pholume> postComment(@Field("pid") String pid, @Field("comment") String comment);

    @Multipart
    @POST("user/avatar")
    Call<User> uploadAvatar(@Part MultipartBody.Part avatar);
}
