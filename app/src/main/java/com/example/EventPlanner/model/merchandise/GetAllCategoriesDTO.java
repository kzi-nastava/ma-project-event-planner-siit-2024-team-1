package com.example.EventPlanner.model.merchandise;

import java.util.List;

public class GetAllCategoriesDTO {
    private List<CategoryOverview> categories;

    public GetAllCategoriesDTO(List<CategoryOverview> categories) {
        this.categories = categories;
    }

    public List<CategoryOverview> getCategories() {
        return categories;
    }

    public void setCategories(List<CategoryOverview> categories) {
        this.categories = categories;
    }
}
