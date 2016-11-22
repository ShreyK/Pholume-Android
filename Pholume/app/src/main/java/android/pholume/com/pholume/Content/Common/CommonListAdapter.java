package android.pholume.com.pholume.Content.Common;

import android.content.Context;
import android.content.Intent;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Content.Profile.ProfileActivity;
import android.pholume.com.pholume.Content.Views.CircleTransform;
import android.pholume.com.pholume.Model.CommentItem;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.databinding.LayoutCommonListItemBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.pholume.com.pholume.Content.Common.CommonListActivity.COMMENTS;

public class CommonListAdapter<T> extends BaseListAdapter<CommonListAdapter.ViewHolder, T> {

    private final String type;
    static class ViewHolder extends RecyclerView.ViewHolder {
        LayoutCommonListItemBinding binding;

        ViewHolder(LayoutCommonListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public CommonListAdapter(Context context, List<T> list, String type) {
        super(context, list);
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutCommonListItemBinding binding = LayoutCommonListItemBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new CommonListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LayoutCommonListItemBinding binding = holder.binding;
        if(type.equals(COMMENTS)) {
            bindComment((CommentItem) list.get(position), binding);
        } else {
            bindUser((User)list.get(position), binding);
        }
    }


    public void bindComment(final CommentItem comment, LayoutCommonListItemBinding binding){
        String url = Constants.BASE_AVATAR + comment.user.avatar;
        Picasso.with(context)
                .load(url)
                .fit().centerCrop().transform(new CircleTransform())
                .into(binding.avatar);
        binding.title.setText(comment.user.username);
        binding.subtitle.setText(comment.comment.comment);
        binding.subtitle.setVisibility(View.VISIBLE);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user", comment.user);
                context.startActivity(intent);
            }
        });
    }

    public void bindUser(final User user, LayoutCommonListItemBinding binding){
        String avatarUrl = Constants.BASE_AVATAR + user.avatar;
        Picasso.with(context)
                .load(avatarUrl)
                .fit().centerCrop().transform(new CircleTransform())
                .into(binding.avatar);
        binding.title.setText(user.username);
        binding.subtitle.setVisibility(View.GONE);

        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });
    }
}
