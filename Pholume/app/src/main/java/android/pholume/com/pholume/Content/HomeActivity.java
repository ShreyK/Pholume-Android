package android.pholume.com.pholume.Content;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.pholume.com.pholume.Content.Capture.CaptureActivity;
import android.pholume.com.pholume.Content.Feed.FeedFragment;
import android.pholume.com.pholume.Content.Profile.ProfileFragment;
import android.pholume.com.pholume.Content.Search.SearchFragment;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.Util;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private static final int PERMISSION_CAMERA = 6969;
    private static final int PERMISSION_RECORD = 1738;

    private static FeedFragment feedFragment;
    private static SearchFragment searchFragment;
    private static ProfileFragment profileFragment;

    private boolean hasCameraPermission = false;
    private boolean hasRecordingPermission = false;

    public class PagerAdapter extends FragmentStatePagerAdapter {

        public PagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    if (feedFragment == null) {
                        feedFragment = new FeedFragment();
                    }
                    return feedFragment;
                case 1:
                    if (searchFragment == null) {
                        searchFragment = new SearchFragment();
                    }
                    return searchFragment;
                case 2:
                    if (profileFragment == null) {
                        profileFragment = new ProfileFragment();
                    }
                    return profileFragment;
                default:
                    Log.e("PagerAdapter", "More than 3 Pages WTF?!");
                    return null;
            }
        }


        @Override
        public int getCount() {
            return 3;
        }
    }

    Activity activity;
    TabLayout tabLayout;
    ViewPager viewPager;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        this.activity = this;

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager());

        hasCameraPermission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
        hasRecordingPermission = ContextCompat.checkSelfPermission(activity,
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED;

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Util.checkCameraHardware(activity)) {
                    if (!hasCameraPermission) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_CAMERA);
                    } else if(!hasRecordingPermission) {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                PERMISSION_RECORD);
                    }

                    startCameraActivty();

                } else {
                    Snackbar.make(view, "No Camera detected", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void startCameraActivty() {
        if (hasCameraPermission && hasRecordingPermission) {
            startActivity(new Intent(HomeActivity.this, CaptureActivity.class));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CAMERA:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasCameraPermission = true;
                    if(hasRecordingPermission){
                        startCameraActivty();
                    } else {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.RECORD_AUDIO},
                                PERMISSION_RECORD);
                    }
                } else {
                    Snackbar.make(this.findViewById(R.id.fab),
                            "Need Camera Permission to take Pholumes!",
                            Snackbar.LENGTH_SHORT).show();
                }
                break;
            case PERMISSION_RECORD:
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hasRecordingPermission = true;
                    if(hasCameraPermission){
                        startCameraActivty();
                    } else {
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.CAMERA},
                                PERMISSION_CAMERA);
                    }
                } else {
                    Snackbar.make(this.findViewById(R.id.fab),
                            "Need Audio Recording Permission to take Pholumes!",
                            Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
