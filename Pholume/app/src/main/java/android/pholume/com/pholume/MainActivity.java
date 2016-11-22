package android.pholume.com.pholume;

import android.content.Intent;
import android.os.Bundle;
import android.pholume.com.pholume.Content.HomeActivity;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.Network.RestManager;
import android.support.v7.app.AppCompatActivity;

import com.crashlytics.android.Crashlytics;

import retrofit2.Call;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RestManager.getInstance().getCurrentUser(new PholumeCallback<User>("CurrentUser") {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    User user = response.body();
                    if (user != null) {
                        PrefManager.getInstance(getApplicationContext()).saveCurrentUser(response.body());
                        logUser(user);
                        startHomeActivity();
                    } else {
                        startLoginActivity();
                    }
                } else {
                    startLoginActivity();
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                super.onFailure(call, t);
                startLoginActivity();
            }
        });
    }

    private void logUser(User user) {
        // You can call any combination of these three methods
        Crashlytics.setUserIdentifier(user.id);
        Crashlytics.setUserEmail(user.email);
        Crashlytics.setUserName(user.username);
    }


    private void startLoginActivity() {
        Intent startLogin = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(startLogin);
        finish();
    }

    private void startHomeActivity() {
        Intent startHome = new Intent(MainActivity.this, HomeActivity.class);
        startActivity(startHome);
        finish();
    }
}
