package com.hotel.hotelService.model;

import java.util.Comparator;

public class AvailableHotel implements Comparator<AvailableHotel> {

    private String provider;
    private String hotelName;
    private double fare;
    private String[] amenities;
    private int rate;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getHotelName() {
        return hotelName;
    }

    public void setHotelName(String hotelName) {
        this.hotelName = hotelName;
    }

    public double getFare() {
        return fare;
    }

    public void setFare(double fare) {
        this.fare = fare;
    }

    public String[] getAmenities() {
        return amenities;
    }

    public void setAmenities(String[] amenities) {
        this.amenities = amenities;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public int compare(AvailableHotel a, AvailableHotel b)
    {
        return b.getRate() - a.getRate();
    }
}
