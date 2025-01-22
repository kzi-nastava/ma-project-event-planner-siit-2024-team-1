package com.example.EventPlanner.model.merchandise;

import com.example.EventPlanner.model.common.Address;
import com.example.EventPlanner.model.common.ReviewOverview;
import com.example.EventPlanner.model.event.EventType;
import com.example.EventPlanner.model.event.EventTypeOverview;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class MerchandiseDetailsDTO {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("specificity")
    @Expose
    private String specificity;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("discount")
    @Expose
    private Integer discount;
    @SerializedName("visible")
    @Expose
    private Boolean visible;
    @SerializedName("available")
    @Expose
    private Boolean available;
    @SerializedName("minDuration")
    @Expose
    private Integer minDuration;
    @SerializedName("maxDuration")
    @Expose
    private Integer maxDuration;
    @SerializedName("reservationDeadline")
    @Expose
    private Integer reservationDeadline;
    @SerializedName("cancellationDeadline")
    @Expose
    private Integer cancellationDeadline;
    @SerializedName("merchandisePhotos")
    @Expose
    private List<MerchandisePhoto> merchandisePhotos;
    @SerializedName("reviews")
    @Expose
    private List<DetailsReviewOverview> reviews;
    @SerializedName("address")
    @Expose
    private Address address;
    @SerializedName("category")
    @Expose
    private CategoryOverview category;
    @SerializedName("eventTypes")
    @Expose
    private List<EventTypeOverview> eventTypes;
    @SerializedName("rating")
    @Expose
    private Double rating;
    @SerializedName("serviceProviderId")
    @Expose
    private Integer serviceProviderId;
    @SerializedName("type")
    @Expose
    private String type;

    public MerchandiseDetailsDTO() {}
    public MerchandiseDetailsDTO(int id, String title, String description, String specificity, Double price, Integer discount, Boolean visible, Boolean available, Integer minDuration, Integer maxDuration, Integer reservationDeadline, Integer cancellationDeadline, List<MerchandisePhoto> merchandisePhotos, List<DetailsReviewOverview> reviews, Address address, CategoryOverview category, List<EventTypeOverview> eventTypes, Double rating, Integer serviceProviderId, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.specificity = specificity;
        this.price = price;
        this.discount = discount;
        this.visible = visible;
        this.available = available;
        this.minDuration = minDuration;
        this.maxDuration = maxDuration;
        this.reservationDeadline = reservationDeadline;
        this.cancellationDeadline = cancellationDeadline;
        this.merchandisePhotos = merchandisePhotos;
        this.reviews = reviews;
        this.address = address;
        this.category = category;
        this.eventTypes = eventTypes;
        this.rating = rating;
        this.serviceProviderId = serviceProviderId;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getSpecificity() {
        return specificity;
    }

    public Double getPrice() {
        return price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public Boolean getVisible() {
        return visible;
    }

    public Boolean getAvailable() {
        return available;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public Integer getReservationDeadline() {
        return reservationDeadline;
    }

    public Integer getCancellationDeadline() {
        return cancellationDeadline;
    }

    public List<MerchandisePhoto> getMerchandisePhotos() {
        return merchandisePhotos;
    }

    public List<DetailsReviewOverview> getReviews() {
        return reviews;
    }

    public Address getAddress() {
        return address;
    }

    public CategoryOverview getCategory() {
        return category;
    }

    public List<EventTypeOverview> getEventTypes() {
        return eventTypes;
    }

    public Double getRating() {
        return rating;
    }

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public String getType() {
        return type;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSpecificity(String specificity) {
        this.specificity = specificity;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setReservationDeadline(Integer reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public void setCancellationDeadline(Integer cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public void setMerchandisePhotos(List<MerchandisePhoto> merchandisePhotos) {
        this.merchandisePhotos = merchandisePhotos;
    }

    public void setReviews(List<DetailsReviewOverview> reviews) {
        this.reviews = reviews;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public void setCategory(CategoryOverview category) {
        this.category = category;
    }

    public void setEventTypes(List<EventTypeOverview> eventTypes) {
        this.eventTypes = eventTypes;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public void setType(String type) {
        this.type = type;
    }
}
