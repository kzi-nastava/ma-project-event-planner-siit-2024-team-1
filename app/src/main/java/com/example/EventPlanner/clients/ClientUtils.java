package com.example.EventPlanner.clients;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.EventPlanner.BuildConfig;
import com.example.EventPlanner.clients.services.event.EventService;
import com.example.EventPlanner.clients.services.merchandise.Category1Service;
import com.example.EventPlanner.clients.services.merchandise.MerchandiseService;
import com.example.EventPlanner.clients.services.merchandise.product.ProductService;
import com.example.EventPlanner.clients.services.merchandise.service.ServiceService;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/v1/";
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public static Category1Service category1Service = retrofit.create(Category1Service.class);
    public static EventService eventService=retrofit.create(EventService.class);
    public static MerchandiseService merchandiseService=retrofit.create(MerchandiseService.class);
    public static ProductService productService=retrofit.create(ProductService.class);
    public static ServiceService serviceService=retrofit.create(ServiceService.class);
}
