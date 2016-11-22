package android.pholume.com.pholume.Network;


import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Model.CommentItem;
import android.pholume.com.pholume.Model.FeedItem;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.util.Log;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestManager {

    private static RestManager sRestManager;
    private static RestService mRestService;

    public static RestManager getInstance() {
        if (sRestManager == null) {
            AddCookiesInterceptor cookiesInterceptor = new AddCookiesInterceptor();
            ReceivedCookiesInterceptor receivedCookiesInterceptor = new ReceivedCookiesInterceptor();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constants.API_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(new OkHttpClient.Builder()
                            .addInterceptor(cookiesInterceptor)
                            .addInterceptor(receivedCookiesInterceptor)
                            .build())
                    .build();
            mRestService = retrofit.create(RestService.class);
            sRestManager = new RestManager();
        }

        return sRestManager;
    }

    public void login(String email, String password, Callback<User> callback) {
        Call<User> call = mRestService.login(email, password);
        call.enqueue(callback);
    }

    public void logout() {
        Call<Void> call = mRestService.logout();
        call.enqueue(new PholumeCallback<Void>("Logout") {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                super.onResponse(call, response);
                if (!response.isSuccessful()) {
                    Log.e("Logout", "Logout failed");
                }
            }
        });
    }

    public void getCurrentUser(Callback<User> callback) {
        Call<User> call = mRestService.getCurrentUser();
        call.enqueue(callback);
    }

    public void getUser(String uid, Callback<User> callback) {
        Call<User> call = mRestService.getUser(uid);
        call.enqueue(callback);
    }


    public void getPholumes(String uid, Callback<List<Pholume>> callback) {
        Call<List<Pholume>> call = mRestService.getPholumes(uid);
        call.enqueue(callback);
    }

    public void getFeed(Callback<List<FeedItem>> callback) {
        Call<List<FeedItem>> call = mRestService.getFeed();
        call.enqueue(callback);
    }

    public void postPholume(MultipartBody.Part pFile, MultipartBody.Part aFile, RequestBody desc,
                            Callback<Pholume> callback) {
        Call<Pholume> call = mRestService.postPholume(pFile, aFile, desc);
        call.enqueue(callback);
    }

    public void postLike(String id, Callback<Pholume> callback) {
        Call<Pholume> call = mRestService.postLike(id);
        call.enqueue(callback);
    }

    public void postComment(String pid, String comment, Callback<Pholume> callback) {
        Call<Pholume> call = mRestService.postComment(pid, comment);
        call.enqueue(callback);
    }

    public void searchForUser(String username, Callback<List<User>> callback) {
        Call<List<User>> call = mRestService.searchForUser(username);
        call.enqueue(callback);
    }

    public void getFollowers(String uid, Callback<List<User>> callback) {
        Call<List<User>> call = mRestService.getFollowers(uid);
        call.enqueue(callback);
    }

    public void getFollowing(String uid, Callback<List<User>> callback) {
        Call<List<User>> call = mRestService.getFollowing(uid);
        call.enqueue(callback);
    }

    public void postFollow(String uid, Callback<User> callback) {
        Call<User> call = mRestService.postFollow(uid);
        call.enqueue(callback);
    }

    public void getComments(String pid, Callback<List<CommentItem>> callback) {
        Call<List<CommentItem>> call = mRestService.getComments(pid);
        call.enqueue(callback);
    }

    public void uploadAvatar(MultipartBody.Part pFile, Callback<User> callback) {
        Call<User> call = mRestService.uploadAvatar(pFile);
        call.enqueue(callback);
    }
}
