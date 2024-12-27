
package com.example.EventPlanner.model.event; ;

import javax.annotation.Generated;
import java.util.List;

import com.example.EventPlanner.model.merchandise.Category2;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class EventType {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("active")
    @Expose
    private Boolean active;
    @SerializedName("recommendedCategories")
    @Expose
    private List<Category2> recommendedCategories;

    /**
     * No args constructor for use in serialization
     * 
     */
    public EventType() {
    }

    public EventType(Integer id, String title, String description, Boolean active, List<Category2> recommendedCategories) {
        super();
        this.id = id;
        this.title = title;
        this.description = description;
        this.active = active;
        this.recommendedCategories = recommendedCategories;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<Category2> getRecommendedCategories() {
        return recommendedCategories;
    }

    public void setRecommendedCategories(List<Category2> recommendedCategories) {
        this.recommendedCategories = recommendedCategories;
    }

}
