package com.example.EventPlanner.clients.services.messages;

import com.example.EventPlanner.model.messages.MessageDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MessagesService {
    @GET("messages")
    Call<List<MessageDTO>> getMessagesBySenderAndRecipient(
            @Query("senderId") int senderId,
            @Query("recipientId") int recipientId
    );
}
