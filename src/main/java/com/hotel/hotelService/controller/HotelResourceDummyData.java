package com.hotel.hotelService.controller;

import com.hotel.hotelService.model.BestHotel;
import com.hotel.hotelService.model.CrazyHotels;
import com.ibm.json.java.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@CrossOrigin
@RestController
public class HotelResourceDummyData {

    @GetMapping("/hotelResource/bestHotel")
    public ResponseEntity<List> bestHotel(@RequestParam("city") String city,
                                          @RequestParam("fromDate") String fromDate,
                                          @RequestParam("toDate") String toDate,
                                          @RequestParam("numberOfAdults") int numberOfAdults) {

        List<BestHotel> bestHotels = new ArrayList<BestHotel>();

        BestHotel bestHotel = new BestHotel();
        bestHotel.setHotel("sheraton");
        bestHotel.setHotelFare(500);
        bestHotel.setHotelRate(4);
        bestHotel.setRoomAmenities("breakfast,launch,dinner,room service");

        BestHotel bestHotel1 = new BestHotel();
        bestHotel1.setHotel("rets");
        bestHotel1.setHotelFare(400);
        bestHotel1.setHotelRate(5);
        bestHotel1.setRoomAmenities("dinner,room service");

        bestHotels.add(bestHotel);
        bestHotels.add(bestHotel1);
        return new ResponseEntity<List>(bestHotels, HttpStatus.OK);
    }

    @GetMapping("/hotelResource/crazyHotel")
    public ResponseEntity<List> crazyHotel(@RequestParam("city") String city,
                                           @RequestParam("from") String fromDate,
                                           @RequestParam(value = "To") String toDate,
                                           @RequestParam(value = "adultsCount") int numberOfAdults) {

        List crazyHotels = new ArrayList<CrazyHotels>();
        String[] amenities = {"breakfast", "launch", "dinner"};

        CrazyHotels crazyHotel = new CrazyHotels();
        crazyHotel.setHotelName("ABC");
        crazyHotel.setRate("***");
        crazyHotel.setPrice(25.0);
        crazyHotel.setDiscount(1.5);
        crazyHotel.setAmenities(amenities);

        CrazyHotels crazyHotel1 = new CrazyHotels();
        crazyHotel1.setHotelName("XYZ");
        crazyHotel1.setRate("****");
        crazyHotel1.setPrice(30.0);
        crazyHotel1.setDiscount(2.5);
        crazyHotel1.setAmenities(amenities);

        crazyHotels.add(crazyHotel);
        crazyHotels.add(crazyHotel1);

        return new ResponseEntity<List>(crazyHotels, HttpStatus.OK);

    }

}
