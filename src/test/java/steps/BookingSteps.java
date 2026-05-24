package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;

public class BookingSteps {

    @Step("ახალი ჯავშნის შექმნა")
    public Response createBooking(String bookingBody) {
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bookingBody)
                .when()
                .post("https://restful-booker.herokuapp.com/booking");

        // სერვერის პასუხი რო დამიბეჭდოს
        response.prettyPrint();
        return response;
    }

    @Step("ჯავშნის წამოღება ID-ით: {bookingId}")
    public Response getBooking(int bookingId) {
        return given()
                .pathParam("id", bookingId)
                .when()
                .get("https://restful-booker.herokuapp.com/booking/{id}");
    }

    @Step("ჯავშნის დამატებითი მოთხოვნების განახლება (PATCH) ID: {bookingId}")
    public Response updateBookingAdditionalNeeds(int bookingId, String token, String patchBody) {
        return given()
                .contentType(ContentType.JSON)
                .header("Cookie", "token=" + token)
                .pathParam("id", bookingId)
                .body(patchBody)
                .when()
                .patch("https://restful-booker.herokuapp.com/booking/{id}");
    }
    @Step("ჯავშნის წაშლა ID-ით: {bookingId}")
    public Response deleteBooking(int bookingId, String token) {
        return given()
                .header("Cookie", "token=" + token)
                .pathParam("id", bookingId)
                .when()
                .delete("https://restful-booker.herokuapp.com/booking/{id}");
    }
}