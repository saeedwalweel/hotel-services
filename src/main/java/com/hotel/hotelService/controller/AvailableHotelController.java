package com.hotel.hotelService.controller;

import com.hotel.hotelService.model.AvailableHotel;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.time.temporal.ChronoUnit.DAYS;


@RestController
@Validated
public class AvailableHotelController {


    @GetMapping("/availableHotel")
    public ResponseEntity<List> availableHotel(@RequestParam("fromDate") @NotBlank(message = "fromDate may not be empty or null") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}", message = "Invalid fromDate pattern, must be YYYY-MM-DD") String fromDate,
                                               @RequestParam("toDate") @NotBlank(message = "toDate may not be empty or null") @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}", message = "Invalid toDate pattern, must be YYYY-MM-DD") String toDate,
                                               @RequestParam("city") @Size(min = 3, max = 3, message = "city should be IATA code with 3 char length") @NotBlank(message = "city may not be empty or null") String city,
                                               @RequestParam("numberOfAdults") int numberOfAdults) {

        HttpResponse<JsonNode> bestHotelResponse = null;
        HttpResponse<JsonNode> crazyHotelResponse = null;
        List availableHotels = new ArrayList<AvailableHotel>();

        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        try {
            bestHotelResponse = getBestHotelResponse(fromDate, toDate, city, numberOfAdults);
            JSONObject jsonObject;
            JSONArray array;
            if (null != bestHotelResponse) {
                jsonObject = new JSONObject(bestHotelResponse.getBody());
                array = jsonObject.getJSONArray("array");
                LocalDate fromDate_localDate = LocalDate.parse(fromDate);
                LocalDate toDate_localDate = LocalDate.parse(toDate);
                for (int i = 0; i < array.length(); i++) {
                    AvailableHotel hotel = new AvailableHotel();
                    hotel.setProvider("BestHotels");
                    hotel.setHotelName(array.getJSONObject(i).getString("hotel"));
                    hotel.setAmenities(array.getJSONObject(i).getString("roomAmenities").split(","));
                    hotel.setRate(array.getJSONObject(i).getInt("hotelRate"));
                    int dayCount = (int) DAYS.between(fromDate_localDate, toDate_localDate);
                    hotel.setFare(Double.parseDouble(df.format(array.getJSONObject(i).getDouble("hotelFare") / dayCount)));
                    availableHotels.add(hotel);
                }
            }

            crazyHotelResponse = getCrazyHotelResponse(fromDate, toDate, city, numberOfAdults);
            if (null != crazyHotelResponse) {
                jsonObject = new org.json.JSONObject(crazyHotelResponse.getBody());
                array = jsonObject.getJSONArray("array");
                double discount;
                double price;
                for (int i = 0; i < array.length(); i++) {
                    List list = new ArrayList<String>();
                    AvailableHotel hotel = new AvailableHotel();
                    hotel.setProvider("CrazyHotels");
                    hotel.setHotelName(array.getJSONObject(i).getString("hotelName"));
                    array.getJSONObject(i).getJSONArray("amenities").forEach(o -> list.add(o));
                    hotel.setAmenities((String[]) list.toArray(new String[list.size()]));
                    hotel.setRate(array.getJSONObject(i).getString("rate").length());
                    discount = array.getJSONObject(i).getDouble("discount");
                    price = array.getJSONObject(i).getDouble("price");
                    hotel.setFare(Double.parseDouble(df.format(discount == 0 ? price : price - ((price * discount) / 100))));
                    availableHotels.add(hotel);
                }
            }

            if (availableHotels.size() > 0) {
                Collections.sort(availableHotels, new AvailableHotel());
            }
        } catch (Exception e) {
            //TODO make custom exception
            e.printStackTrace();
        }
        return new ResponseEntity<List>(availableHotels, HttpStatus.OK);
    }


    private HttpResponse<JsonNode> getBestHotelResponse(String fromDate, String toDate, String city, int numberOfAdults) throws UnirestException {
        HttpResponse<JsonNode> bestHotelResponse = null;
        bestHotelResponse = Unirest.get("http://localhost:8099/hotelResource/bestHotel")
                .header("accept", "application/json")
                .queryString("city", city)
                .queryString("fromDate", fromDate)
                .queryString("toDate", toDate)
                .queryString("numberOfAdults", numberOfAdults)
                .asJson();

        return bestHotelResponse;
    }

    private HttpResponse<JsonNode> getCrazyHotelResponse(String fromDate, String toDate, String city, int numberOfAdults) throws UnirestException {
        HttpResponse<JsonNode> crazyHotelResponse = null;
        crazyHotelResponse = Unirest.get("http://localhost:8099/hotelResource/crazyHotel")
                .header("accept", "application/json")
                .queryString("city", city)
                .queryString("from", fromDate)
                .queryString("To", toDate)
                .queryString("adultsCount", numberOfAdults)
                .asJson();
        return crazyHotelResponse;
    }
}
