package com.restassured.basics;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static com.jayway.restassured.RestAssured.given;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.lessThan;
import static org.junit.Assert.assertFalse;

public class GarageTest extends FunctionalTest {

    @Test
    public void basicPingTest() {
        given().when().get("/garage").then().statusCode(200);
    }

    @Test
    public void invalidParkingSpace() {
        given().when().get("/garage/slots/999")
                .then().statusCode(404);
    }

    @Test
    public void verifyNameOfGarage() {
        given().when().get("/garage").then()
                .body(containsString("Acme garage"));
    }

    @Test
    public void verifyNameStructured() {
        given().when().get("/garage").then()
                .body("name",equalTo("Acme garage"));
    }

    @Test
    public void verifySlotsOfGarage() {
        given().when().get("/garage").then().
                body("info.slots",equalTo(150))
                .body("info.status",equalTo("open"));
    }

    @Test
    public void verifyTopLevelURL() {
        given().when().get("/garage").then()
                .body("name",equalTo("Acme garage"))
                .body("info.slots",equalTo(150))
                .body("info.status",equalTo("open"))
                .statusCode(200);
    }

    @Test
    public void aCarGoesIntoTheGarage() {
        Map<String,String> car = new HashMap<>();
        car.put("plateNumber", "xyx1111");
        car.put("brand", "audi");
        car.put("colour", "red");

        given()
                .contentType("application/json")
                .body(car)
                .when().post("/garage/slots").then()
                .statusCode(201);
    }

    @Test
    public void aCarGoesIntoTheGarageStructured() {
        Map<String,String> car = new HashMap<>();
        car.put("plateNumber", "xyx1111");
        car.put("brand", "audi");
        car.put("colour", "red");

        given()
                .contentType("application/json")
                .body(car)
                .when().post("/garage/slots").then()
                .body("empty",equalTo(false))
                .body("position",lessThan(150));
    }

    @Test
    public void aCarIsRegisteredInTheGarage() {
        Car car = new Car();
        car.setPlateNumber("xyx1111");
        car.setBrand("audi");
        car.setColour("red");

        Slot slot = given()
                .contentType("application/json")
                .body(car)
                .when().post("/garage/slots")
                .as(Slot.class);

        assertFalse(slot.isEmpty());
        assertTrue(slot.getPosition() < 150);

    }

    @Test
    public void aCarLeaves() {
        given().pathParam("slotID", 27)
                .when().delete("/garage/slots/{slotID}")
                .then().statusCode(200);

    }

    @Test
    public void aCarEntersAndThenLeaves() {
        Car car = new Car();
        car.setPlateNumber("xyx1111");
        car.setBrand("audi");
        car.setColour("red");

        int positionTakenInGarage = given()
                .contentType("application/json")
                .body(car)
                .when().post("/garage/slots").then()
                .body("empty",equalTo(false))
                .extract().path("position");

        given().pathParam("slotID", positionTakenInGarage)
                .when().delete("/garage/slots/{slotID}").then()
                .statusCode(200);

    }
}
