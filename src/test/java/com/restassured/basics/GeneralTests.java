package com.restassured.basics;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ResponseBody;
import com.jayway.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.CoreMatchers.*;

public class GeneralTests {

    private String baseOpenWeatherUrl = "http://api.openweathermap.org/data/2.5/";
    private String apiKey = "&APPID=9eb4623733534137be148ac0922f35c1";

    @Test
    public void openWeatherMapTest() {
        RestAssured.baseURI = baseOpenWeatherUrl;
        RequestSpecification httprequest = RestAssured.given();
        Response response = httprequest.get("weather?q=London,uk"+apiKey);
        ResponseBody body = response.body();
        System.out.println("The response is: " + body.asString());
    }

    @Test
    public void parseOpenWeatherMapTest() {
        when().
                get(baseOpenWeatherUrl+"weather?q=London,uk"+apiKey).
                then().
                body("coord.lon", is(-0.13f),//checking that its a float
                        "weather.description", hasItems("moderate rain", "mist", "light intensity drizzle"),
                        "weather.description[2]", is("mist")
                );
    }

    @Test
    public void WeatherMessageBody()
    {
        RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/Hyderabad");
        ResponseBody body = response.getBody();
        System.out.println("Response Body is: " + body.asString());
        String bodyAsString = body.asString();
        Assert.assertTrue(bodyAsString.toLowerCase().contains("hyderabad"));
    }

    @Test
    public void VerifyCityInJsonResponse()
    {
        RestAssured.baseURI = "http://restapi.demoqa.com/utilities/weather/city";
        RequestSpecification httpRequest = RestAssured.given();
        Response response = httpRequest.get("/Hyderabad");

        JsonPath jsonPathEvaluator = response.jsonPath();

        String city = jsonPathEvaluator.get("City");

        System.out.println("City received from Response " + city);

        Assert.assertEquals("Correct city name received in the Response", "Hyderabad", city);
    }
}
