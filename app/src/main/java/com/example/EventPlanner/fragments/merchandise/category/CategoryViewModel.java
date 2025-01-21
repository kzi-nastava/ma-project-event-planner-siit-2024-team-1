package com.example.EventPlanner.fragments.merchandise.category;

import android.util.Log;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.clients.ClientUtils;
import com.example.EventPlanner.model.merchandise.CategoryOverview;
import com.example.EventPlanner.model.merchandise.CreateCategoryRequest;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel {
    private final MutableLiveData<ArrayList<CategoryOverview>> approvedCategoryLiveData = new MutableLiveData<>();
    private final MutableLiveData<ArrayList<CategoryOverview>> pendingCategoryLiveData = new MutableLiveData<>();
    private final MutableLiveData<CategoryOverview> selectedCategory = new MutableLiveData<>();

    public CategoryViewModel() { }
    public LiveData<ArrayList<CategoryOverview>> getAllApprovedCategories() { return approvedCategoryLiveData; }
    public LiveData<ArrayList<CategoryOverview>> getAllPendingCategories() { return pendingCategoryLiveData; }
    public LiveData<CategoryOverview> getSelectedCategory() { return selectedCategory; }

    public LiveData<ArrayList<CategoryOverview>> getAllCategoriesByType(CategoryList.CategoryType categoryType) {
        if(categoryType == CategoryList.CategoryType.APPROVED) {
            return getAllApprovedCategories();
        }else {
            return getAllPendingCategories();
        }
    }

    public void getApprovedCategories() {
        Call<List<CategoryOverview>> call = ClientUtils.categoryService.getApproved();
        call.enqueue(new Callback<List<CategoryOverview>>() {
            @Override
            public void onResponse(Call<List<CategoryOverview>> call, Response<List<CategoryOverview>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    approvedCategoryLiveData.postValue(new ArrayList<>(response.body()));
                } else {
                    Log.e("CategoryViewModel", "Failed to load approved Categories: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryOverview>> call, Throwable throwable) {
                Log.e("CategoryViewModel", "Error fetching approved Categories: " + throwable.getMessage());
            }
        });
    }

    public void getPendingCategories() {
        Call<List<CategoryOverview>> call = ClientUtils.categoryService.getPending();
        call.enqueue(new Callback<List<CategoryOverview>>() {
            @Override
            public void onResponse(Call<List<CategoryOverview>> call, Response<List<CategoryOverview>> response) {
                if(response.isSuccessful() && response.body() != null) {
                    pendingCategoryLiveData.postValue(new ArrayList<>(response.body()));
                } else {
                    Log.e("CategoryViewModel", "Failed to load pending Categories: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<CategoryOverview>> call, Throwable throwable) {
                Log.e("CategoryViewModel", "Error fetching pending Categories: " + throwable.getMessage());
            }
        });
    }

    public void getById(int categoryId) {
        Call<CategoryOverview> call = ClientUtils.categoryService.getById(categoryId);
        call.enqueue(new Callback<CategoryOverview>() {
            @Override
            public void onResponse(Call<CategoryOverview> call, Response<CategoryOverview> response) {
                if(response.isSuccessful() && response.body() != null) {
                    selectedCategory.postValue(response.body());
                }else {
                    Log.e("CategoryViewModel", "Failed to get category: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryOverview> call, Throwable throwable) {
                Log.e("CategoryViewModel", "Error getting category: " + throwable.getMessage());
            }
        });
    }

    public void replaceCategory(int categoryId, int replacedCategoryId) {
        Call<ResponseBody> call = ClientUtils.categoryService.replaceCategory(categoryId, replacedCategoryId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    boolean found = false;
                    ArrayList<CategoryOverview> currentListApproved = approvedCategoryLiveData.getValue();
                    if(currentListApproved != null) {
                        for(int i = 0; i < currentListApproved.size(); i++) {
                            if(currentListApproved.get(i).getId() == categoryId) {
                                currentListApproved.remove(i);
                                found = true;
                                break;
                            }
                        }
                        approvedCategoryLiveData.setValue(currentListApproved);
                    }

                    if(found) {
                        ArrayList<CategoryOverview> currentListPending = pendingCategoryLiveData.getValue();
                        if(currentListPending != null) {
                            for(int i = 0; i < currentListPending.size(); i++) {
                                if(currentListPending.get(i).getId() == categoryId) {
                                    currentListPending.remove(i);
                                    break;
                                }
                            }
                            pendingCategoryLiveData.setValue(currentListPending);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {

            }
        });
    }

    public void approveCategory(int categoryId) {
        Call<CategoryOverview> call = ClientUtils.categoryService.approveCategory(categoryId);
        call.enqueue(new Callback<CategoryOverview>() {
            @Override
            public void onResponse(Call<CategoryOverview> call, Response<CategoryOverview> response) {
                if(response.isSuccessful() && response.body() != null) {
                    ArrayList<CategoryOverview> currentListApproved = approvedCategoryLiveData.getValue();
                    if(currentListApproved == null) {
                        currentListApproved = new ArrayList<>();
                    }
                    CategoryOverview approvedCategory = response.body();
                    currentListApproved.add(approvedCategory);
                    approvedCategoryLiveData.setValue(currentListApproved);

                    ArrayList<CategoryOverview> currentListPending = pendingCategoryLiveData.getValue();
                    if(currentListPending != null) {
                        currentListPending.removeIf(category -> category.getId() == approvedCategory.getId());
                        pendingCategoryLiveData.setValue(currentListPending);
                    }
                }else {
                    Log.e("CategoryViewModel", "Failed to approve category: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryOverview> call, Throwable throwable) {
                Log.e("CategoryViewModel", "Error approving category: " + throwable.getMessage());
            }
        });
    }

    public void createCategory(CreateCategoryRequest dto) {
        Call<CategoryOverview> call = ClientUtils.categoryService.create(dto);
        call.enqueue(new Callback<CategoryOverview>() {
            @Override
            public void onResponse(Call<CategoryOverview> call, Response<CategoryOverview> response) {
                if(response.isSuccessful() && response.body() != null) {
                    CategoryOverview createdCategory = response.body();
                    if(createdCategory.isPending()) {
                        ArrayList<CategoryOverview> currentList = pendingCategoryLiveData.getValue();
                        if(currentList == null) {
                            currentList = new ArrayList<>();
                        }
                        currentList.add(createdCategory);
                        pendingCategoryLiveData.setValue(currentList);
                    }else {
                        ArrayList<CategoryOverview> currentList = approvedCategoryLiveData.getValue();
                        if(currentList == null) {
                            currentList = new ArrayList<>();
                        }
                        currentList.add(createdCategory);
                        approvedCategoryLiveData.setValue(currentList);
                    }
                }else {
                    Log.e("CategoryViewModel", "Failed to create new Category: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryOverview> call, Throwable throwable) {
                Log.e("CategoryViewModel", "Error creating new Category: " + throwable.getMessage());
            }
        });
    }

    public void updateCategory(int categoryId, CreateCategoryRequest dto) {
        Call<CategoryOverview> call = ClientUtils.categoryService.updateCategory(categoryId, dto);
        call.enqueue(new Callback<CategoryOverview>() {
            @Override
            public void onResponse(Call<CategoryOverview> call, Response<CategoryOverview> response) {
                if(response.isSuccessful() && response.body() != null) {
                    CategoryOverview updatedCategory = response.body();
                    if(updatedCategory.isPending()) {
                        ArrayList<CategoryOverview> currentList = pendingCategoryLiveData.getValue();
                        if (currentList != null) {
                            for(int i = 0; i < currentList.size(); i++) {
                                if(currentList.get(i).getId() == updatedCategory.getId()) {
                                    currentList.set(i, updatedCategory);
                                    break;
                                }
                            }
                            pendingCategoryLiveData.setValue(currentList);
                        }
                    }else {
                        ArrayList<CategoryOverview> currentList = approvedCategoryLiveData.getValue();
                        if (currentList != null) {
                            for(int i = 0; i < currentList.size(); i++) {
                                if(currentList.get(i).getId() == updatedCategory.getId()) {
                                    currentList.set(i, updatedCategory);
                                    break;
                                }
                            }
                            approvedCategoryLiveData.setValue(currentList);
                        }
                    }
                }else {
                    Log.e("CategoryViewModel", "Failed to update category: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<CategoryOverview> call, Throwable throwable) {
                Log.e("CategoryViewModel", "Error updating category: " + throwable.getMessage());
            }
        });
    }

    public void deleteCategory(int categoryId) {
        Call<ResponseBody> call = ClientUtils.categoryService.deleteCategory(categoryId);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()) {
                    boolean found = false;
                    ArrayList<CategoryOverview> currentListApproved = approvedCategoryLiveData.getValue();
                    if(currentListApproved != null) {
                        for(int i = 0; i < currentListApproved.size(); i++) {
                            if(currentListApproved.get(i).getId() == categoryId) {
                                currentListApproved.remove(i);
                                found = true;
                                break;
                            }
                        }
                        approvedCategoryLiveData.setValue(currentListApproved);
                    }

                    if(!found) {
                        ArrayList<CategoryOverview> currentListPending = pendingCategoryLiveData.getValue();
                        if(currentListPending != null) {
                            for(int i = 0; i < currentListPending.size(); i++) {
                                if(currentListPending.get(i).getId() == categoryId) {
                                    currentListPending.remove(i);
                                    break;
                                }
                            }
                            pendingCategoryLiveData.setValue(currentListPending);
                        }
                    }
                }else {
                    Log.e("CategoryViewModel", "Failed to delete category: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                Log.e("CategoryViewModel", "Error deleting category: " + throwable.getMessage());
            }
        });
    }
}
