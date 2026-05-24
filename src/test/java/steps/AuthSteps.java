package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import java.util.HashMap;
import java.util.Map;
import static io.restassured.RestAssured.given;

public class AuthSteps {

    @Step("ავტორიზაციის ტოკენის გენერირება admin მომხმარებლისთვის")
    public String createToken() {
        Map<String, String> authData = new HashMap<>();
        authData.put("username", "admin");
        authData.put("password", "password123");

        Response response = given()
                .contentType(ContentType.JSON)
                .body(authData)
                .when()
                .post("https://restful-booker.herokuapp.com/auth");

        return response.jsonPath().getString("token");
    }
}