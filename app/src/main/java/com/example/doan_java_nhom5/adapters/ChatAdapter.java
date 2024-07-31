package com.example.doan_java_nhom5.adapters;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan_java_nhom5.databinding.ItemContainerReceivedMassageBinding;
import com.example.doan_java_nhom5.databinding.ItemContainerSendMessaageBinding;
import com.example.doan_java_nhom5.models.ChatMessage;

import java.util.List;

public class ChatAdapter extends  RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<ChatMessage> chatMessages;
    private Bitmap receiverProfileImage;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVER = 2;

    public void setReceiverProfileImage(Bitmap bitmap){
        receiverProfileImage = bitmap;
    }

    public ChatAdapter(List<ChatMessage> chatMessages, Bitmap receiverProfileImage, String senderId) {
        this.chatMessages = chatMessages;
        this.receiverProfileImage = receiverProfileImage;
        this.senderId = senderId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewholder(ItemContainerSendMessaageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }else {
            return new ReceivedMessageViewHolder(ItemContainerReceivedMassageBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(getItemViewType(position) == VIEW_TYPE_SENT){
            ((SentMessageViewholder) holder).setData(chatMessages.get(position));
        }else {
            ((ReceivedMessageViewHolder) holder).setData(chatMessages.get(position), receiverProfileImage);
        }

    }

    @Override
    public int getItemCount() {
        return chatMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(chatMessages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        }else{
            return  VIEW_TYPE_RECEIVER;
        }
    }

    static class SentMessageViewholder extends RecyclerView.ViewHolder{
        private final ItemContainerSendMessaageBinding binding;

        SentMessageViewholder(ItemContainerSendMessaageBinding itemContainerSendMessaageBinding){
            super(itemContainerSendMessaageBinding.getRoot());
            binding = itemContainerSendMessaageBinding;
        }
        void setData(ChatMessage chatMessage){
            binding.textMassage.setText(chatMessage.message);
            binding.textDataTime.setText(chatMessage.dateTime);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ItemContainerReceivedMassageBinding binding;
        ReceivedMessageViewHolder(ItemContainerReceivedMassageBinding itemContainerReceivedMassageBinding) {
            super (itemContainerReceivedMassageBinding.getRoot());
            binding = itemContainerReceivedMassageBinding;
        }

        void setData(ChatMessage chatMessage, Bitmap receiverProfileImage) {
            binding.textMessage.setText(chatMessage.message);
            binding.textDataTime.setText(chatMessage.dateTime);
            if(receiverProfileImage != null){
                binding.imageProfile.setImageBitmap(receiverProfileImage);
            }
        }
    }
}
