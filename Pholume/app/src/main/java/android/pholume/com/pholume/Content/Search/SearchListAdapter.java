package android.pholume.com.pholume.Content.Search;

import android.content.Context;
import android.content.Intent;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Content.Common.BaseListAdapter;
import android.pholume.com.pholume.Content.Profile.ProfileActivity;
import android.pholume.com.pholume.Content.Views.CircleTransform;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.R;
import android.pholume.com.pholume.databinding.ItemSearchBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

class SearchListAdapter extends BaseListAdapter<SearchListAdapter.ViewHolder, User> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        ItemSearchBinding binding;

        ViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    SearchListAdapter(Context context, List<User> list) {
        super(context, list);
    }

    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSearchBinding binding = ItemSearchBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new SearchListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final SearchListAdapter.ViewHolder holder, int position) {
        final User user = list.get(position);
        ItemSearchBinding binding = holder.binding;
        binding.usernameText.setText(user.username);
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context == null) return;
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });

        String avatarUrl = Constants.BASE_AVATAR + user.avatar;
        Picasso.with(context).load(avatarUrl).fit().centerCrop().transform(new CircleTransform())
                .placeholder(R.drawable.ic_profile)
                .into(binding.avatarImage);
    }
}