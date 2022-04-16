package com.svalero.gestitaller.domain.dto;

import androidx.room.Ignore;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

public class OrderDTO implements Comparable {

    private int id;     // Order
    private LocalDate date; // Order
    private String clientNameSurname;  // Client
    private String bikeBrandModel;    // Client
    private String bikeLicensePlate;    // Bike
    private byte[] bikeImageOrder;   // Bike
    private String description; // Order

    @Ignore
    public OrderDTO() {
    }

    public OrderDTO(int id, LocalDate date, String clientNameSurname, String bikeBrandModel, String bikeLicensePlate, byte[] bikeImageOrder, String description) {
        this.id = id;
        this.date = date;
        this.clientNameSurname = clientNameSurname;
        this.bikeBrandModel = bikeBrandModel;
        this.bikeLicensePlate = bikeLicensePlate;
        this.bikeImageOrder = bikeImageOrder;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getClientNameSurname() {
        return clientNameSurname;
    }

    public void setClientNameSurname(String clientNameSurname) {
        this.clientNameSurname = clientNameSurname;
    }

    public String getBikeBrandModel() {
        return bikeBrandModel;
    }

    public void setBikeBrandModel(String bikeBrandModel) {
        this.bikeBrandModel = bikeBrandModel;
    }

    public byte[] getBikeImageOrder() {
        return bikeImageOrder;
    }

    public void setBikeImageOrder(byte[] bikeImageOrder) {
        this.bikeImageOrder = bikeImageOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderDTO orderDTO = (OrderDTO) o;
        return id == orderDTO.id && date.equals(orderDTO.date);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date);
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String toString() {
        return "OrderDTO{" +
                "id=" + id +
                ", date=" + date +
                ", clientNameSurname='" + clientNameSurname + '\'' +
                ", bikeBrandModel='" + bikeBrandModel + '\'' +
                ", description='" + description + '\'' +
                ", bikeImageOrder=" + Arrays.toString(bikeImageOrder) +
                '}';
    }

    public String getBikeLicensePlate() {
        return bikeLicensePlate;
    }

    public void setBikeLicensePlate(String bikeLicensePlate) {
        this.bikeLicensePlate = bikeLicensePlate;
    }
}
