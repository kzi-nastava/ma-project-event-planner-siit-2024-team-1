package com.example.EventPlanner.fragments.merchandise.service;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.EventPlanner.R;
import com.example.EventPlanner.adapters.merchandise.service.ServiceAdapter;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.databinding.FragmentServiceListBinding;
import com.example.EventPlanner.fragments.event.EventDecorator;
import com.example.EventPlanner.model.merchandise.service.Service;
import com.example.EventPlanner.model.merchandise.service.ServiceOverview;
import com.example.EventPlanner.model.merchandise.service.Timeslot;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.sql.Time;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ServiceList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ServiceList extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentServiceListBinding serviceListBinding;
    private ServiceViewModel serviceViewModel;
    private MaterialCalendarView calendarView;

    public ServiceList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ServiceList.
     */
    // TODO: Rename and change types and number of parameters
    public static ServiceList newInstance(String param1, String param2) {
        ServiceList fragment = new ServiceList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        serviceListBinding = FragmentServiceListBinding.inflate(getLayoutInflater());
        serviceViewModel = new ViewModelProvider(requireActivity()).get(ServiceViewModel.class);
        RecyclerView recyclerView = serviceListBinding.serviceListVertical;

        calendarView = serviceListBinding.calendarView;
        calendarView.setDateTextAppearance(R.color.white);
        serviceViewModel.getServices().observe(getViewLifecycleOwner(), services -> {
            ServiceAdapter adapter = new ServiceAdapter(requireContext(), services);
            recyclerView.setAdapter(adapter);

            calendarView.removeDecorators();
            List<CalendarDay> serviceDates = new ArrayList<>();
            for(ServiceOverview service : services) {
                for(Timeslot timeslot: getServiceTimeSlots(service.getId())) {
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                    LocalDateTime startDate = LocalDateTime.parse(timeslot.getStartTime(), formatter);

                    CalendarDay calendarStartDate = CalendarDay.from(startDate.getYear(),
                            startDate.getMonthValue() - 1,
                            startDate.getDayOfMonth());
                    serviceDates.add(calendarStartDate);
                }
            }
            calendarView.addDecorator(new EventDecorator(
                    ContextCompat.getColor(requireContext(), R.color.accent_color),
                    serviceDates
            ));
            adapter.notifyDataSetChanged();
        });

        serviceViewModel.getAllBySp(JwtService.getIdFromToken());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        return serviceListBinding.getRoot();
    }

    private List<Timeslot> getServiceTimeSlots(int serviceId) {
        List<Timeslot> timeslots = new ArrayList<>();
        Call<List<Timeslot>> timeSlotCall = ClientUtils.serviceService.getTimeslots(serviceId);
        timeSlotCall.enqueue(new Callback<List<Timeslot>>() {
            @Override
            public void onResponse(Call<List<Timeslot>> call, Response<List<Timeslot>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    timeslots.addAll(response.body());
                }else {
                    Log.e("ServiceList", "Failed to fetch time slots: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Timeslot>> call, Throwable throwable) {
                Log.e("ServiceList", "Error fetching time slots: " + throwable.getMessage());
            }
        });
        return timeslots;
    }
}