package com.example.EventPlanner.fragments.merchandise.product;

import androidx.lifecycle.ViewModel;

import com.example.EventPlanner.DummyEventGenerator;
import com.example.EventPlanner.model.merchandise.product.Product;

import java.util.ArrayList;

public class ProductViewModel extends ViewModel {
    private ArrayList<Product> products;

    public ProductViewModel() {
        // Set products with dummy data for now
        setProducts(new ArrayList<>(DummyEventGenerator.DummyProductGenerator.createDummyProduct(5)));
    }

    // Returns all products
    public ArrayList<Product> getAll() {
        return products;
    }

    // Finds a product by ID
    public Product findProductById(int id) {
        for (Product product : products) {
            if (product.getId() != null && product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    // Adds a new product
    public void saveProduct(Product product) {
        if (product.getId() == null) {
            // Assign a new ID (for simplicity, this just increments the size)
            product.setId(products.size() + 1);  // Assuming IDs are sequential
        }
        products.add(product);
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
