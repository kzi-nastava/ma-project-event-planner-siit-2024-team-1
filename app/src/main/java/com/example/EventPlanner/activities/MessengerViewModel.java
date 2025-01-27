package com.example.EventPlanner.activities;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.messages.MessageDTO;
import com.example.EventPlanner.model.user.UserOverview;
import com.example.EventPlanner.model.user.UserReportOverview;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MessengerViewModel extends ViewModel {
    private final MutableLiveData<UserOverview> messagedUser=new MutableLiveData<>();
    private final MutableLiveData<List<MessageDTO>> messages=new MutableLiveData<>();
    public LiveData<UserOverview> getMessagedUser(){
        return messagedUser;
    }

    public LiveData<List<MessageDTO>> getMessages(){
        return messages;
    }

    public void getChatUser(int id){
        Call<UserOverview> call = ClientUtils.userService.getChatUser(id);
        call.enqueue(new Callback<UserOverview>() {
            @Override
            public void onResponse(Call<UserOverview> call, Response<UserOverview> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messagedUser.postValue(response.body());  // This gets just the list of events

                } else {

                }
            }

            @Override
            public void onFailure(Call<UserOverview> call, Throwable t) {
                Log.d("errrrror",t.getMessage());
            }
        });
    }

    public void getMessagesFromSenderAndRecepient(int senderId,int recipientId){
        Call<List<MessageDTO>> call = ClientUtils.messagesService.getMessagesBySenderAndRecipient(senderId,recipientId);
        call.enqueue(new Callback<List<MessageDTO>>() {
            @Override
            public void onResponse(Call<List<MessageDTO>> call, Response<List<MessageDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    messages.setValue(response.body());  // This gets just the list of events
                } else {

                }
            }

            @Override
            public void onFailure(Call<List<MessageDTO>> call, Throwable t) {
                Log.d("errrrror",t.getMessage());
            }
        });
    }

    public void addMessage(MessageDTO message) {
        if (messages.getValue() == null) {
            messages.setValue(new ArrayList<>());
        }
        List<MessageDTO> updatedMessages = new ArrayList<>(messages.getValue());
        updatedMessages.add(message);
        messages.setValue(updatedMessages);
    }
}
