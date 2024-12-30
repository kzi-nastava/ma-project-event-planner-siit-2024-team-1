package com.example.EventPlanner.model.merchandise.product;

import java.util.List;
import javax.annotation.Generated;

import com.example.EventPlanner.model.common.Address;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class UpdateProductRequest {

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
    @SerializedName("automaticReservation")
    @Expose
    private Boolean automaticReservation;
    @SerializedName("serviceProviderId")
    @Expose
    private Integer serviceProviderId;
    @SerializedName("merchandisePhotos")
    @Expose
    private List<Integer> merchandisePhotos;
    @SerializedName("eventTypesIds")
    @Expose
    private List<Integer> eventTypesIds;
    @SerializedName("address")
    @Expose
    private Address address;

    /**
     * No args constructor for use in serialization
     *
     */
    public UpdateProductRequest() {
    }

    public UpdateProductRequest(String title, String description, String specificity, Double price, Integer discount, Boolean visible, Boolean available, Integer minDuration, Integer maxDuration, Integer reservationDeadline, Integer cancellationDeadline, Boolean automaticReservation, Integer serviceProviderId, List<Integer> merchandisePhotos, List<Integer> eventTypesIds, Address address) {
        super();
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
        this.automaticReservation = automaticReservation;
        this.serviceProviderId = serviceProviderId;
        this.merchandisePhotos = merchandisePhotos;
        this.eventTypesIds = eventTypesIds;
        this.address = address;
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

    public String getSpecificity() {
        return specificity;
    }

    public void setSpecificity(String specificity) {
        this.specificity = specificity;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public Boolean getVisible() {
        return visible;
    }

    public void setVisible(Boolean visible) {
        this.visible = visible;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public Integer getMinDuration() {
        return minDuration;
    }

    public void setMinDuration(Integer minDuration) {
        this.minDuration = minDuration;
    }

    public Integer getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Integer maxDuration) {
        this.maxDuration = maxDuration;
    }

    public Integer getReservationDeadline() {
        return reservationDeadline;
    }

    public void setReservationDeadline(Integer reservationDeadline) {
        this.reservationDeadline = reservationDeadline;
    }

    public Integer getCancellationDeadline() {
        return cancellationDeadline;
    }

    public void setCancellationDeadline(Integer cancellationDeadline) {
        this.cancellationDeadline = cancellationDeadline;
    }

    public Boolean getAutomaticReservation() {
        return automaticReservation;
    }

    public void setAutomaticReservation(Boolean automaticReservation) {
        this.automaticReservation = automaticReservation;
    }

    public Integer getServiceProviderId() {
        return serviceProviderId;
    }

    public void setServiceProviderId(Integer serviceProviderId) {
        this.serviceProviderId = serviceProviderId;
    }

    public List<Integer> getMerchandisePhotos() {
        return merchandisePhotos;
    }

    public void setMerchandisePhotos(List<Integer> merchandisePhotos) {
        this.merchandisePhotos = merchandisePhotos;
    }

    public List<Integer> getEventTypesIds() {
        return eventTypesIds;
    }

    public void setEventTypesIds(List<Integer> eventTypesIds) {
        this.eventTypesIds = eventTypesIds;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}