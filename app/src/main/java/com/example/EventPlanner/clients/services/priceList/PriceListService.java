package com.example.EventPlanner.clients.services.priceList;

import com.example.EventPlanner.model.priceList.PriceListItem;
import com.example.EventPlanner.model.priceList.UpdatePriceListItemRequest;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PriceListService {
    @GET("priceList/{serviceProviderId}")
    Call<List<PriceListItem>> getPriceList(@Path("serviceProviderId") int serviceProviderId);

    @PUT("priceList/update/{merchandiseId}/{serviceProviderId}")
    Call<List<PriceListItem>> updatePriceListItem(@Path("merchandiseId") int merchandiseId,
                                                  @Path("serviceProviderId") int serviceProviderId,
                                                  @Body UpdatePriceListItemRequest dto);
}
