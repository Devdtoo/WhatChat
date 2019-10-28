package com.devdtoo.whatchat.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.devdtoo.whatchat.MessageActivity;
import com.devdtoo.whatchat.Model.Chat;
import com.devdtoo.whatchat.Model.User;
import com.devdtoo.whatchat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT_RC = 0;
    public static final int MSG_TYPE_RIGHT_RC = 1;

    private Context mContext;
    private List<Chat> mChat;
    private String imageUrl;

    FirebaseUser fCurrentUser;

    //  Constructor
    public MessageAdapter(Context mContext, List<Chat> mChat, String imageUrl) {
        this.mContext = mContext;
        this.mChat = mChat;
        this.imageUrl = imageUrl;
    }
    //    ..............................................................................................
//  These Methods are Implemented on MessageAdapter Class
    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT_RC) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_right, parent, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(mContext).inflate(R.layout.chat_item_left, parent, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {

        Chat chat = mChat.get(position);
        holder.show_message.setText(chat.getMessage());

        if (imageUrl.equals("default")) {
            holder.profile_pic.setImageResource(R.drawable.defaultprofile);
        }else {
            Glide.with(mContext).load(imageUrl).into(holder.profile_pic);
        }

//        It Checks for Last Message sent
        if (position == mChat.size() - 1 ) {
            if (chat.isSeen()) {
                holder.txt_seen.setText("seen");
            } else {
                holder.txt_seen.setText("Delivered");
            }
        }else {
            holder.txt_seen.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return mChat.size();
    }

    //    ..............................................................................................
//    This class is the one that holds the Views with ID
    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView show_message;
        public ImageView profile_pic;
        public TextView txt_seen;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profile_pic = itemView.findViewById(R.id.profile_pic);
            txt_seen = itemView.findViewById(R.id.txt_seen);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fCurrentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mChat.get(position).getSender().equals(fCurrentUser.getUid())) {
            return MSG_TYPE_RIGHT_RC;
        } else {
            return MSG_TYPE_LEFT_RC;
        }
    }
}
