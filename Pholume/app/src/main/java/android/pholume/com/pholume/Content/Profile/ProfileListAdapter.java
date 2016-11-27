package android.pholume.com.pholume.Content.Profile;

import android.content.Context;
import android.content.Intent;
import android.pholume.com.pholume.Constants;
import android.pholume.com.pholume.Content.Common.BaseListAdapter;
import android.pholume.com.pholume.Content.Common.PholumeActivity;
import android.pholume.com.pholume.Model.Pholume;
import android.pholume.com.pholume.Model.User;
import android.pholume.com.pholume.PrefManager;
import android.pholume.com.pholume.databinding.CardPholumeProfileBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

import static android.support.v7.widget.RecyclerView.NO_POSITION;

class ProfileListAdapter extends BaseListAdapter<ProfileListAdapter.ViewHolder, Pholume> {

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardPholumeProfileBinding binding;

        ViewHolder(CardPholumeProfileBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    ProfileListAdapter(Context context, List<Pholume> list) {
        super(context, list);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardPholumeProfileBinding binding = CardPholumeProfileBinding.inflate(
                LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Pholume pholume = list.get(position);
        CardPholumeProfileBinding binding = holder.binding;
        binding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (context == null) return;
                Intent intent = new Intent(context, PholumeActivity.class);
                if (holder.getAdapterPosition() == NO_POSITION) return;
                intent.putExtra("pholume", list.get(holder.getAdapterPosition()));
                User user;
                try {
                    user = PrefManager.getInstance().getCurrentUser();
                } catch (NullPointerException e) {
                    user = null;
                }
                intent.putExtra("user", user);
                context.startActivity(intent);
            }
        });
        Picasso.with(context).load(Constants.BASE_PHOTO + pholume.photoUrl).fit().centerCrop().into(binding.pholumeImage);
    }
}