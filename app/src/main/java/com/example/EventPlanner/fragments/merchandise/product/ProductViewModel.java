package com.example.EventPlanner.fragments.merchandise.product;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyEventGenerator;
import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.clients.JwtService;
import com.example.EventPlanner.model.common.PageResponse;
import com.example.EventPlanner.model.event.CreateEventTypeRequest;
import com.example.EventPlanner.model.event.CreatedEventResponse;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.example.EventPlanner.model.event.UpdateEventTypeRequest;
import com.example.EventPlanner.model.merchandise.MerchandiseOverview;
import com.example.EventPlanner.model.merchandise.product.CreateProductRequest;
import com.example.EventPlanner.model.merchandise.product.CreateProductResponse;
import com.example.EventPlanner.model.merchandise.product.Product;
import com.example.EventPlanner.model.merchandise.product.ProductOverview;
import com.example.EventPlanner.model.merchandise.product.UpdateProductRequest;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductViewModel extends ViewModel {
    private ArrayList<Product> products;
    private final MutableLiveData<ArrayList<ProductOverview>> productsLiveData = new MutableLiveData<>();
    private final MutableLiveData<ProductOverview> selectedProduct = new MutableLiveData<>();

    private final MutableLiveData<ArrayList<MerchandiseOverview>> mercLiveData = new MutableLiveData<>();
    public ProductViewModel() {
        // Set products with dummy data for now
        setProducts(new ArrayList<>(DummyEventGenerator.DummyProductGenerator.createDummyProduct(5)));
    }

    public LiveData<ArrayList<ProductOverview>> getProducts(){
        return productsLiveData;
    }

    public LiveData<ArrayList<MerchandiseOverview>> getMerc(){
        return mercLiveData;
    }

    public LiveData<ProductOverview> getSelectedProduct() {
        return selectedProduct;
    }

    // Fetch all event types from the server
    public void getAllBySp(int spId) {
        Call<List<ProductOverview>> call = ClientUtils.productService.getBySp(spId);
        call.enqueue(new Callback<List<ProductOverview>>() {
            @Override
            public void onResponse(Call<List<ProductOverview>> call, Response<List<ProductOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    productsLiveData.postValue(new ArrayList<>(response.body()));
                } else {
                    Log.e("ProductViewModel", "Failed to fetch Products: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<ProductOverview>> call, Throwable t) {
                Log.e("ProductViewModel", "Error fetching Products", t);
            }
        });
    }

    public void getFavorites() {
        Call<List<MerchandiseOverview>> call = ClientUtils.merchandiseService.getFavorites(JwtService.getIdFromToken());
        call.enqueue(new Callback<List<MerchandiseOverview>>() {
            @Override
            public void onResponse(Call<List<MerchandiseOverview>> call, Response<List<MerchandiseOverview>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mercLiveData.postValue(new ArrayList<>(response.body()));
                } else {
                    Log.e("ProductViewModel", "Failed to fetch Merchandise: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<MerchandiseOverview>> call, Throwable t) {
                Log.e("ProductViewModel", "Error fetching Merchandise", t);
            }
        });
    }

    // Find an event type by ID
    public void findProductById(int id) {
        Call<ProductOverview> call = ClientUtils.productService.getById(id);
        call.enqueue(new Callback<ProductOverview>() {
            @Override
            public void onResponse(Call<ProductOverview> call, Response<ProductOverview> response) {
                if (response.isSuccessful() && response.body() != null) {
                    selectedProduct.postValue(response.body());
                } else {
                    Log.e("ProductViewModel", "Failed to fetch Product by ID: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ProductOverview> call, Throwable t) {
                Log.e("ProductViewModel", "Error fetching Product by ID", t);
            }
        });
    }

    // Add or update an event type
    public void saveProduct(CreateProductRequest dto) {
        Call<CreateProductResponse> call1 = ClientUtils.productService.create(dto);
        call1.enqueue(new Callback<CreateProductResponse>() {
            @Override
            public void onResponse(Call<CreateProductResponse> call, Response<CreateProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ArrayList<ProductOverview> currentList = productsLiveData.getValue();
                    if (currentList == null) {
                        currentList = new ArrayList<>();
                    }
                    CreateProductResponse created = response.body();
                    currentList.add(new ProductOverview(created.getId(), created.getTitle(), created.getDescription(), created.getSpecificity(), created.getPrice(),
                            created.getDiscount(), created.getVisible(), created.getAvailable(), created.getMinDuration(), created.getMaxDuration(),
                            created.getReservationDeadline(), created.getCancellationDeadline(), created.getAutomaticReservation(), created.getMerchandisePhotos(),
                            created.getEventTypes(), created.getAddress(), created.getCategory()));
                    productsLiveData.setValue(currentList);
                } else {
                    // Handle error cases
                    Log.e("Product Create Error", "Response not successful: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<CreateProductResponse> call, Throwable throwable) {
                // Handle network errors
                Log.e("Product Create Failure", "Error: " + throwable.getMessage());
            }
        });
    }

    public void updateProduct(int id, UpdateProductRequest dto) {
        Call<CreateProductResponse> call = ClientUtils.productService.update(id, dto); // assuming you have an update endpoint
        call.enqueue(new Callback<CreateProductResponse>() {
            @Override
            public void onResponse(Call<CreateProductResponse> call, Response<CreateProductResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update the list of event types
                    ArrayList<ProductOverview> currentList = productsLiveData.getValue();
                    CreateProductResponse created = response.body();
                    ProductOverview overview = new ProductOverview(created.getId(), created.getTitle(), created.getDescription(), created.getSpecificity(), created.getPrice(),
                            created.getDiscount(), created.getVisible(), created.getAvailable(), created.getMinDuration(), created.getMaxDuration(),
                            created.getReservationDeadline(), created.getCancellationDeadline(), created.getAutomaticReservation(), created.getMerchandisePhotos(),
                            created.getEventTypes(), created.getAddress(), created.getCategory());
                    if (currentList != null) {
                        // Replace the old event type with the updated one
                        for (int i = 0; i < currentList.size(); i++) {
                            if (currentList.get(i).getId() == response.body().getId()) {
                                currentList.set(i, overview);
                                break;
                            }
                        }
                        productsLiveData.setValue(currentList);
                    }
                } else {
                    Log.e("ProductViewModel", "Failed to update Product: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CreateProductResponse> call, Throwable t) {
                Log.e("ProductViewModel", "Error updating Product", t);
            }
        });
    }

    public void availProduct(int id) {
        Call<Boolean> call = ClientUtils.productService.avail(id); // assuming you have an update endpoint
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    Log.e("ProductViewModel", "Failed to avail Product: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("ProductViewModel", "Error availing Product", t);
            }
        });
    }

    public void showProduct(int id) {
        Call<Boolean> call = ClientUtils.productService.show(id); // assuming you have an update endpoint
        call.enqueue(new Callback<Boolean>() {
            @Override
            public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                if (response.isSuccessful() && response.body() != null) {

                } else {
                    Log.e("ProductViewModel", "Failed to show Product: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Boolean> call, Throwable t) {
                Log.e("ProductViewModel", "Error showing Product", t);
            }
        });
    }

    // Deletes a product
    public void deleteProduct(Product product) {
        products.remove(product);
    }

    // Set the list of products
    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }
}
