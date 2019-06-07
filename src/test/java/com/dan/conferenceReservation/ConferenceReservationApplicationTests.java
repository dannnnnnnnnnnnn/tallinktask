package com.dan.conferenceReservation;

import io.restassured.http.ContentType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import io.restassured.RestAssured.*;
import io.restassured.matcher.RestAssuredMatchers.*;
import org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.containsString;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ConferenceReservationApplicationTests {
	@Test
	public void whenCreatingParticipantValidEntity_thenOK() {
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("name", "Test Person");
		jsonAsMap.put("birthDate", "1997-10-26");
		given().contentType(ContentType.JSON).
				body(jsonAsMap).
				when().
				post("/participants").
				then().
				statusCode(201);
	}

	@Test
	public void whenCreatingParticipantInvalidEntity_thenBadRequest() {
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("name", "");
		jsonAsMap.put("birthDate", "1997-10-26");
		given().contentType(ContentType.JSON).
				body(jsonAsMap).
				when().
				post("/participants").
				then().
				statusCode(400);
	}

	@Test
	public void whenCreatingParticipantParamsNull_thenBadRequest() {
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("name", null);
		jsonAsMap.put("birthDate", null);
		given().contentType(ContentType.JSON).
				body(jsonAsMap).
				when().
				post("/participants").
				then().
				statusCode(400);
	}

	@Test
	public void whenRequestGetParticipantById_thenOK() {
		when().request("GET", "/participants/1").then().statusCode(200);
	}
	@Test
	public void whenRequestGetParticipantById_thenNotFound() {
		when().request("GET", "/participants/1000").then().statusCode(404);
	}
	@Test
	public void whenRequestDeleteParticipantById_thenNotFound() {
		when().request("DELETE", "/participants/1000").then().statusCode(404);
	}


	@Test
	public void whenCreatingRoomValidEntity_thenOK() {
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("name", "MS Victoria");
		jsonAsMap.put("location", "Tallinn");
		jsonAsMap.put("seats", "200");
		given().contentType(ContentType.JSON).
				body(jsonAsMap).
				when().
				post("/rooms").
				then().
				statusCode(200);
	}
	@Test
	public void whenCreatingRoomInvalidEntity_badRequest() {
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("name", null);
		jsonAsMap.put("location", null);
		jsonAsMap.put("seats", null);
		given().contentType(ContentType.JSON).
				body(jsonAsMap).
				when().
				post("/rooms").
				then().
				statusCode(400);
	}
	@Test
	public void whenRequestGetRoomById_thenOK() {
		when().request("GET", "/rooms/1").then().statusCode(200);
	}
	@Test
	public void whenRequestGetRoomById_thenNotFound() {
		when().request("GET", "/rooms/1000").then().statusCode(404);
	}
	@Test
	public void whenRequestDeleteRoomById_thenNotFound() {
		when().request("DELETE", "/rooms/1000").then().statusCode(404);
	}
	@Test
	public void whenCreatingConferenceValidEntity_thenOK() {
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("name", "Test conference ");
		jsonAsMap.put("startTime", "2019-05-10T16:20:00");
		jsonAsMap.put("endTime", "2019-05-10T17:20:00");
		given().contentType(ContentType.JSON).body(jsonAsMap).when().post("/conferences").then().statusCode(201);
	}
	@Test
	public void whenCreatingConferenceInvalidEntity_badRequest() {
		Map<String, Object> jsonAsMap = new HashMap<>();
		jsonAsMap.put("name", null);
		jsonAsMap.put("startTime", null);
		jsonAsMap.put("endTime", null);
		given().contentType(ContentType.JSON).body(jsonAsMap).when().post("/conferences").then().statusCode(400);
	}
	@Test
	public void whenAddingParticipantToConference_thenOK() {
		when().post("/conferences/1/participants/1").then().statusCode(200);
	}
	@Test
	public void whenAddingRoomToConference_thenOK() {
		when().post("/conferences/1/room/1").then().statusCode(200);
	}
	@Test
	public void whenAddingRoomToConferenceRoomNotFound_badRequest() {
		when().post("/conferences/1/room/1000").then().statusCode(400);
	}
	@Test
	public void whenAddingRoomToConferenceConferenceNotFound_badRequest() {
		when().post("/conferences/1000/room/1").then().statusCode(400);
	}




}
