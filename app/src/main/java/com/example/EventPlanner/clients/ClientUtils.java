package com.example.EventPlanner.clients;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.EventPlanner.BuildConfig;
import com.example.EventPlanner.clients.services.auth.AuthService;
import com.example.EventPlanner.clients.services.photo.PhotoService;
import com.example.EventPlanner.clients.services.common.ReviewService;
import com.example.EventPlanner.clients.services.user.UserService;
import com.example.EventPlanner.clients.services.event.EventService;
import com.example.EventPlanner.clients.services.eventType.EventTypeService;
import com.example.EventPlanner.clients.services.merchandise.CategoryService;
import com.example.EventPlanner.clients.services.merchandise.MerchandiseService;
import com.example.EventPlanner.clients.services.merchandise.product.ProductService;
import com.example.EventPlanner.clients.services.merchandise.service.ServiceService;

public class ClientUtils {
    public static final String SERVICE_API_PATH = "http://" + BuildConfig.IP_ADDR + ":8080/api/v1/";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();

    public static final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(SERVICE_API_PATH)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build();

    public static CategoryService categoryService = retrofit.create(CategoryService.class);

    public static EventService eventService = retrofit.create(EventService.class);

    public static MerchandiseService merchandiseService = retrofit.create(MerchandiseService.class);

    public static ProductService productService = retrofit.create(ProductService.class);

    public static ServiceService serviceService = retrofit.create(ServiceService.class);

    public static EventTypeService eventTypeService = retrofit.create(EventTypeService.class);

    public static AuthService authService = retrofit.create(AuthService.class);

    public static UserService userService = retrofit.create(UserService.class);
  
    public static PhotoService photoService = retrofit.create(PhotoService.class);
  
    public static ReviewService reviewService=retrofit.create(ReviewService.class);
}
