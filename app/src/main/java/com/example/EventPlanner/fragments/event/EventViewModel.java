package com.example.EventPlanner.fragments.event;

import android.util.Log;

import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.EventOverview;
import com.example.EventPlanner.model.event.InvitationResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventViewModel extends ViewModel {
    public void sendInvitation(String email,int eventId){
        Call<InvitationResponse> call = ClientUtils.eventService.sendInvite(email,eventId);
        call.enqueue(new Callback<InvitationResponse>() {
            @Override
            public void onResponse(Call<InvitationResponse> call, Response<InvitationResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("invitationToken", response.body().getInviteToken());

                } else {

                }
            }

            @Override
            public void onFailure(Call<InvitationResponse> call, Throwable t) {
                Log.d("jaje",t.getMessage());
            }
        });
    }
}
