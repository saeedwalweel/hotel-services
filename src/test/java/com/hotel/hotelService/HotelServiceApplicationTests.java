package com.hotel.hotelService;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class HotelServiceApplicationTests {

	@Test
	public void contextLoads() {
		try {
			HttpResponse<JsonNode> bestHotelResponse = Unirest.get("http://localhost:8099/availableHotel")
					.header("accept", "application/json")
					.queryString("fromDate","20-08-2019")
					.queryString("toDate", "2019-09-20")
					.queryString("city", "AJO")
					.queryString("numberOfAdults", 3)
					.asJson();
			System.out.println(bestHotelResponse.getBody());
		} catch (UnirestException e) {
			e.printStackTrace();
		} catch (Exception e){
			e.printStackTrace();
		}
	}

}
