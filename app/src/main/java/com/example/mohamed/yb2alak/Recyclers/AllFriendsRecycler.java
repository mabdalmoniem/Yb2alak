package com.example.mohamed.yb2alak.Recyclers;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mohamed.yb2alak.AddTransactionActivity;
import com.example.mohamed.yb2alak.LoginActivity;
import com.example.mohamed.yb2alak.R;
import com.example.mohamed.yb2alak.ViewFriendsActivity;
import com.example.mohamed.yb2alak.api.mapping.user.models.Friend;

import java.util.List;

public class AllFriendsRecycler extends RecyclerView.Adapter<AllFriendsRecycler.ViewHolder> {

    private List<Friend> mFriends;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context mContext;

    // data is passed into the constructor
    public AllFriendsRecycler(Context context, List<Friend> friends) {
        this.mInflater = LayoutInflater.from(context);
        this.mFriends = friends;
        this.mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.friend_recycler_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Friend friend = mFriends.get(position);
        holder.tv_friend_name.setText(friend.getName());
        holder.tv_friend_email.setText(friend.getEmail());

        holder.tv_add_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(mContext, AddTransactionActivity.class);
                i.putExtra("friend",mFriends.get(position));
                mContext.startActivity(i);
            }
        });
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mFriends.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_friend_name;
        TextView tv_friend_email;
        TextView tv_add_transaction;

        ViewHolder(View itemView) {
            super(itemView);
            tv_friend_name = itemView.findViewById(R.id.tv_friend_name);
            tv_friend_email = itemView.findViewById(R.id.tv_friend_email);
            tv_add_transaction = itemView.findViewById(R.id.tv_add_transaction);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
//            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    Friend getItem(int id) {
        return mFriends.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
