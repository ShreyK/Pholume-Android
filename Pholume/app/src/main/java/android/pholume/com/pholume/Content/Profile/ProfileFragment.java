package android.pholume.com.pholume.Content.Profile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Content.Common.CommonListActivity;
import android.pholume.com.pholume.Content.HomeActivity;
import android.pholume.com.pholume.Content.Views.CircleTransform;
import android.pholume.com.pholume.LoginActivity;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.Network.PholumeCallback;
import android.pholume.com.pholume.Network.RestManager;
import android.pholume.com.pholume.PrefManager;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.FragmentProfileBinding;
import android.support.annotation.ColorRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileFragment extends Fragment {

    private int GALLERY_REQUEST_CODE = 100;

    private
    @ColorRes
    int BUTTON_COLOR_R = R.color.even_lighter_grey;
    private
    @ColorRes
    int BUTTON_FONT_COLOR_R = R.color.black;

    ProfileListAdapter adapter;
    ArrayList<Pholume> pholumeList;

    private Activity activity;
    private User currentUser;
    private User user;
    private FragmentProfileBinding binding;
    private RecyclerView recyclerView;
    private View.OnClickListener buttonListener;
    private View.OnClickListener followListener;
    private View.OnClickListener logoutListener;

    @Override
    public void onResume() {
        super.onResume();
        bindUser();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        activity = getActivity();

        if (activity instanceof HomeActivity) {
            user = PrefManager.getInstance().getCurrentUser();
        } else {
            Bundle bundle = getArguments();
            if (bundle != null) {
                user = bundle.getParcelable("user");
            }
        }

        pholumeList = new ArrayList<>();
        currentUser = PrefManager.getInstance().getCurrentUser();
        adapter = new ProfileListAdapter(getActivity(), pholumeList, user);
        recyclerView = (RecyclerView) binding.getRoot().findViewById(R.id.recycler_view);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchUserPholumes();
            }
        });
        setupListeners();
        fetchUserPholumes();
        return binding.getRoot();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            InputStream iStream = null;
            try {
                iStream = getActivity().getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            byte[] inputData = getBytes(iStream);

            RequestBody pFile = RequestBody.create(MediaType.parse("multipart/form-data"), inputData);
            MultipartBody.Part photoBody =
                    MultipartBody.Part.createFormData("avatar", "avatar", pFile);

            RestManager.getInstance().uploadAvatar(photoBody, new PholumeCallback<User>("UploadAvatar") {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    super.onResponse(call, response);
                    if (response.isSuccessful()) {
                        bindAvatar(response.body());
                    } else {
                        Log.e("UploadAvatar", response.message());
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    super.onFailure(call, t);
                    Log.e("I'm sad", "so sad");
                }
            });

            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setupListeners() {
        binding.profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfileImage();
            }
        });
        binding.followersContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommonListActivity.class);
                intent.putExtra(CommonListActivity.TYPE_EXTRA, CommonListActivity.FOLLOWERS);
                intent.putExtra(CommonListActivity.USER_EXTRA, user);
                startActivity(intent);
            }
        });
        binding.followingContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), CommonListActivity.class);
                intent.putExtra(CommonListActivity.TYPE_EXTRA, CommonListActivity.FOLLOWING);
                intent.putExtra(CommonListActivity.USER_EXTRA, user);
                startActivity(intent);
            }
        });
        logoutListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestManager.getInstance().logout();
                PrefManager.getInstance().logoutAndClearUserCookies();
                startActivity(new Intent(getActivity(), LoginActivity.class));
                getActivity().finish();
            }
        };
        followListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RestManager.getInstance().postFollow(user.id, new PholumeCallback<User>("FOLLOW") {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        super.onResponse(call, response);
                        if (response.isSuccessful()) {
                            user = response.body();
                            if (user.followers.contains(currentUser.id)) {
                                currentUser.following.add(user.id);
                            } else {
                                currentUser.following.remove(user.id);
                            }
                            PrefManager.getInstance().saveCurrentUser(currentUser);
                            bindUser();
                        }
                    }
                });
            }
        };
    }

    private void fetchUserPholumes() {
        if (user == null) return;
        RestManager.getInstance().getPholumes(user.id, new PholumeCallback<List<Pholume>>("GetPholumes") {
            @Override
            public void onResponse(Call<List<Pholume>> call, Response<List<Pholume>> response) {
                super.onResponse(call, response);
                binding.swipeRefreshLayout.setRefreshing(false);
                if (response.isSuccessful()) {
                    bindData(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Pholume>> call, Throwable t) {
                super.onFailure(call, t);
                binding.swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void fetchUser() {
        PholumeCallback getUser = new PholumeCallback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                super.onResponse(call, response);
                if (response.isSuccessful()) {
                    user = response.body();
                    if (user.id.equals(currentUser.id)) {
                        currentUser = user;
                    }
                    bindUser();
                }
            }
        };
        if (getActivity() instanceof HomeActivity) {
            getUser.setString("Current User");
            RestManager.getInstance().getCurrentUser(getUser);
        } else {
            getUser.setString("User");
            RestManager.getInstance().getUser(user.id, getUser);
        }
    }

    private void bindUser() {
        String buttonText;
        if (getActivity() instanceof HomeActivity) {
            buttonText = "Logout";
            buttonListener = logoutListener;
            binding.profileImage.setClickable(true);
        } else {
            binding.profileImage.setClickable(true);
            if (currentUser.isFollowing(user.id)) {
                buttonText = "Following";
                buttonListener = followListener;
                BUTTON_COLOR_R = R.color.even_lighter_grey;
                BUTTON_FONT_COLOR_R = R.color.black;
            } else {
                buttonText = "Follow";
                buttonListener = followListener;
                BUTTON_COLOR_R = R.color.colorAccent;
                BUTTON_FONT_COLOR_R = R.color.white;
            }
        }
        binding.profileName.setText(user.username);
        binding.followersValue.setText(user.getNumOfFollowers());
        binding.followingValue.setText(user.getNumOfFollowing());

        bindAvatar(user);

        binding.profileButton.setText(buttonText);
        binding.profileButton.setBackgroundResource(BUTTON_COLOR_R);
        binding.profileButton.setTextColor(getResources().getColor(BUTTON_FONT_COLOR_R));
        binding.profileButton.setOnClickListener(buttonListener);
    }

    private void bindData(List<Pholume> pholumes) {
        pholumeList = (ArrayList<Pholume>) pholumes;
        adapter.setData(pholumeList);
        adapter.notifyDataSetChanged();
    }

    private void bindAvatar(User user) {
        if (user.avatar == null || user.avatar.isEmpty()) return;
        if (PrefManager.getInstance().getCurrentUser().id.equals(user.id)) {
            PrefManager.getInstance().saveCurrentUser(user);
        }
        String url = Constants.BASE_AVATAR + user.avatar;
        Picasso.with(getActivity())
                .load(url)
                .fit().centerCrop().transform(new CircleTransform())
                .into(binding.profileImage);
    }

    private void editProfileImage() {
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT, null);
        galleryIntent.setType("image/*");
        galleryIntent.addCategory(Intent.CATEGORY_OPENABLE);

        Intent chooser = new Intent(Intent.ACTION_CHOOSER);
        chooser.putExtra(Intent.EXTRA_INTENT, galleryIntent);
        chooser.putExtra(Intent.EXTRA_TITLE, "title");
        startActivityForResult(chooser, GALLERY_REQUEST_CODE);
    }

    private byte[] getBytes(InputStream inputStream) {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        try {
            while ((len = inputStream.read(buffer)) != -1) {
                byteBuffer.write(buffer, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteBuffer.toByteArray();
    }
}
