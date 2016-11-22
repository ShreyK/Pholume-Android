package android.pholume.com.pholume.Network;

import android.util.Log;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PholumeCallback<T> implements Callback<T> {

    private String callName;

    public PholumeCallback(String callName) {
        super();
        this.callName = callName;
    }

    public PholumeCallback() {
        super();
        this.callName = "";
    }

    public void setString(String name){
        callName = name;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        if(!response.isSuccessful()){
            Log.e(callName, response.message());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        Log.e(callName, t.toString());
    }
}
