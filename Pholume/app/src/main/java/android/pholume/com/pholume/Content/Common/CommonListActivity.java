package android.pholume.com.pholume.Content.Common;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.pholume.com.pholume.Content.Views.DividerItemDecoration;
import android.pholume.com.pholume.Model.CommentItem;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.Network.RestManager;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.ActivityBaseListBinding;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonListActivity extends AppCompatActivity {

    private final static String LOG = CommonListActivity.class.getSimpleName();
    public final static String TYPE_EXTRA = "type";
    public final static String USER_EXTRA = "user";
    public final static String PHOLUME_EXTRA = "pholume";
    public final static String COMMENTS = "COMMENTS";
    public final static String FOLLOWERS = "FOLLOWERS";
    public final static String FOLLOWING = "FOLLOWING";


    ActivityBaseListBinding binding;
    CommonListAdapter adapter;
    String type;
    static User user;
    static Pholume pholume;

    ArrayList<CommentItem> comments;
    Callback<List<User>> userCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_list);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_base_list);
        Bundle bundle = getIntent().getExtras();
        if (bundle == null) {
            Log.e(LOG, "No Bundle sent");
            return;
        }
        type = bundle.getString(TYPE_EXTRA);
        if (type == null) {
            Log.e(LOG, "No List Type sent");
            return;
        }

        userCallback = new PholumeCallback<List<User>>(type) {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                super.onResponse(call, response);
                binding.swipeRefreshLayout.setRefreshing(false);
                binding.progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {
                    adapter.setData(response.body());
                    adapter.notifyDataSetChanged();
                }
                updateViews();
            }
        };

        switch (type) {
            case COMMENTS:
                pholume = bundle.getParcelable(PHOLUME_EXTRA);
                binding.commentTextView.setVisibility(View.VISIBLE);
                binding.title.setText(pholume.description + " - Comments");
                adapter = new CommonListAdapter<>(this, new ArrayList<CommentItem>(), COMMENTS);
                setupCommentEditText();
                fetchComments(pholume.id);
                binding.swipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                fetchComments(pholume.id);
                            }
                        });
                break;
            case FOLLOWERS:
                binding.commentTextView.setVisibility(View.GONE);
                user = bundle.getParcelable(USER_EXTRA);
                binding.title.setText(user.username + " - Followers");
                adapter = new CommonListAdapter<>(this, new ArrayList<User>(), FOLLOWERS);
                fetchFollowers();
                binding.swipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                fetchFollowers();
                            }
                        });
                break;
            case FOLLOWING:
                binding.commentTextView.setVisibility(View.GONE);
                user = bundle.getParcelable(USER_EXTRA);
                binding.title.setText(user.username + " - Following");
                adapter = new CommonListAdapter<>(this, new ArrayList<User>(), FOLLOWING);
                fetchFollowing();
                binding.swipeRefreshLayout.setOnRefreshListener(
                        new SwipeRefreshLayout.OnRefreshListener() {
                            @Override
                            public void onRefresh() {
                                fetchFollowing();
                            }
                        });
                break;
        }

        binding.recyclerView.addItemDecoration(new DividerItemDecoration(this, R.drawable.divider));
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateViews() {
        if (adapter.getItemCount() > 0) {
            binding.emptyTextView.setVisibility(View.GONE);
            binding.recyclerView.setVisibility(View.VISIBLE);
        } else {
            binding.emptyTextView.setVisibility(View.VISIBLE);
            binding.recyclerView.setVisibility(View.GONE);
        }
    }

    public void setupCommentEditText() {
        binding.commentTextView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if ((keyEvent.getAction() == KeyEvent.ACTION_DOWN) && (i == KeyEvent.KEYCODE_ENTER)) {
                    String comment = binding.commentTextView.getText().toString();
                    if (!comment.isEmpty()) {
                        Log.d("COMMENT", comment);
                        postComment(pholume.id, comment);
                    }
                    binding.commentTextView.setText("");
                    return true;
                }
                return false;
            }
        });
    }

    public void fetchComments(String pid) {
        binding.progressBar.setVisibility(View.VISIBLE);
        RestManager.getInstance().getComments(pid, new PholumeCallback<List<CommentItem>>("GetComments") {
            @Override
            public void onResponse(Call<List<CommentItem>> call, Response<List<CommentItem>> response) {
                binding.swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    adapter.setData(response.body());
                    adapter.notifyDataSetChanged();
                    binding.progressBar.setVisibility(View.GONE);
                }
                updateViews();
            }
        });
    }

    public void postComment(String pid, String comment) {
        binding.progressBar.setVisibility(View.VISIBLE);
        RestManager.getInstance().postComment(pid, comment, new PholumeCallback<Pholume>("PostComment") {
            @Override
            public void onResponse(Call<Pholume> call, Response<Pholume> response) {
                if (response.isSuccessful()) {
                    binding.progressBar.setVisibility(View.GONE);
                    fetchComments(response.body().id);
                }
            }
        });
    }

    public void fetchFollowers() {
        binding.progressBar.setVisibility(View.VISIBLE);
        RestManager.getInstance().getFollowers(user.id, userCallback);
    }

    public void fetchFollowing() {
        binding.progressBar.setVisibility(View.VISIBLE);
        RestManager.getInstance().getFollowing(user.id, userCallback);
    }
}
