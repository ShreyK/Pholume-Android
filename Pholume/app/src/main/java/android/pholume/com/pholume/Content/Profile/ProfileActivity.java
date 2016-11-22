package android.pholume.com.pholume.Content.Profile;

import android.os.Bundle;
import android.pholume.com.pholume.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    ProfileFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        fragment = new ProfileFragment();
        fragment.setArguments(getIntent().getExtras());
        setFragment(fragment);
    }

    protected void setFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.profile_fragment_container, fragment);
        fragmentTransaction.commit();
    }
}
