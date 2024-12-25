
package com.example.EventPlanner.product; ;

import javax.annotation.Generated;
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
    private Object recommendedCategories;

    /**
     * No args constructor for use in serialization
     * 
     */
    public EventType() {
    }

    public EventType(Integer id, String title, String description, Boolean active, Object recommendedCategories) {
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

    public Object getRecommendedCategories() {
        return recommendedCategories;
    }

    public void setRecommendedCategories(Object recommendedCategories) {
        this.recommendedCategories = recommendedCategories;
    }

}
