package android.pholume.com.pholume.Content.Common;

import android.content.Context;
import android.os.Bundle;
import android.pholume.com.pholume.Content.Feed.FeedFragment;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.R;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;

public class PholumeActivity extends AppCompatActivity {

    private static final String LOG = PholumeActivity.class.getSimpleName();

    public static final String USER_EXTRA = "USER";
    public static final String LIST_EXTRA = "LIST";
    public static final String POSITION_EXTRA = "POSITION";

    private Context mContext;
    private FeedFragment fragment;

    private ArrayList<Pholume> pholumes;
    private User user;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pholume);
        mContext = this;

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pholumes = bundle.getParcelableArrayList(LIST_EXTRA);
            position = bundle.getInt(POSITION_EXTRA, 0);
            user = bundle.getParcelable(USER_EXTRA);
        } else {
            Log.e(LOG, "No bundle passed");
            return;
        }
        if (fragment == null) {
            fragment = FeedFragment.newInstance(user, pholumes, position, true);
        }
        setFragment(fragment);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

}
