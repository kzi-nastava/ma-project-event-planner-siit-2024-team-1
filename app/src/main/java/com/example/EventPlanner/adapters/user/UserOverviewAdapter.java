package com.example.EventPlanner.adapters.user;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.EventPlanner.R;
import com.example.EventPlanner.activities.MessengerActivity;
import com.example.EventPlanner.activities.SendInvitationScreen;
import com.example.EventPlanner.activities.ServiceDetailsActivity;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;
import com.example.EventPlanner.model.user.UserOverview;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserOverviewAdapter extends RecyclerView.Adapter<UserOverviewAdapter.ViewHolder> {
    private List<UserOverview> aUsers;
    private Context context;

    public UserOverviewAdapter(Context context, List<UserOverview> users) {
        this.context = context;
        aUsers = users;
    }

    @NonNull
    @Override
    public UserOverviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_user_overview_card, parent, false);
        return new UserOverviewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserOverviewAdapter.ViewHolder holder, int position) {
        UserOverview user = aUsers.get(position);

//        if (user.getPhotos() != null && !user.getPhotos()) {
//            holder.merchandiseImage.setImageResource(R.drawable.dinja);
//        }
        holder.userOverviewImage.setImageResource(R.drawable.dinja);
        holder.userOverviewName.setText(user.getFirstName()+" "+user.getLastName());
        holder.userOverviewEmail.setText(user.getEmail());
        holder.userOverviewCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessengerActivity.class);
            intent.putExtra("USER_ID", user.getId());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return aUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout userOverviewCard;
        ImageView userOverviewImage;
        TextView userOverviewName;
        TextView userOverviewEmail;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            userOverviewCard=itemView.findViewById(R.id.user_overview_card);
            userOverviewImage=itemView.findViewById(R.id.user_overview_image);
            userOverviewName=itemView.findViewById(R.id.user_overview_name);
            userOverviewEmail=itemView.findViewById(R.id.user_overview_email);
        }
    }
}
