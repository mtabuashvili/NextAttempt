package tests;

import steps.AuthSteps;
import steps.BookingSteps;
import utils.BaseTest;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

@Epic("სასტუმროს რეზერვაციების API")
@Feature("ტესტირება - პირველი ნაწილი")
public class BookingTests extends BaseTest {

    private AuthSteps authSteps;
    private BookingSteps bookingSteps;

    // ტესტ 2 ის ცვლადები.
    public static String token;
    public static int bookingId;

    @BeforeClass(alwaysRun = true)
    public void initSteps() {
        authSteps = new AuthSteps();
        bookingSteps = new BookingSteps();
        token = authSteps.createToken();
    }

    @Test(priority = 1, groups = {"smoke", "regression"})
    @Story("ჯავშნის შექმნა და მისი სრული ვალიდაცია/განახლება")
    @Description("ეს ტესტი ამოწმებს ჯავშნის წარმატებულ შექმნას, მონაცემების სისწორეს და სერვისების PATCH განახლებას.")
    public void testCreateAndModifyBooking() {

        String initialBookingBody = "{\n" +
                "    \"firstname\" : \"Skillwill\",\n" +
                "    \"lastname\" : \"Academy\",\n" +
                "    \"totalprice\" : 1000,\n" +
                "    \"depositpaid\" : true,\n" +
                "    \"bookingdates\" : {\n" +
                "        \"checkin\" : \"2026-06-01\",\n" +
                "        \"checkout\" : \"2026-06-05\"\n" +
                "    },\n" +
                "    \"additionalneeds\" : \"Breakfast\"\n" +
                "}";


        Response createResponse = bookingSteps.createBooking(initialBookingBody);
        createResponse.then().statusCode(200);


        bookingId = createResponse.jsonPath().getInt("bookingid");


        Response getResponse = bookingSteps.getBooking(bookingId);
        getResponse.then().statusCode(200)
                .body("firstname", equalTo("Skillwill"))
                .body("lastname", equalTo("Academy"))
                .body("depositpaid", equalTo(true));

        //განახლება
        String patchBody = "{\n" +
                "    \"additionalneeds\" : \"Breakfast, Lunch, Room cleaning service\"\n" +
                "}";

        Response patchResponse = bookingSteps.updateBookingAdditionalNeeds(bookingId, token, patchBody);
        patchResponse.then().statusCode(200);


        bookingSteps.getBooking(bookingId).then().statusCode(200)
                .body("additionalneeds", equalTo("Breakfast, Lunch, Room cleaning service"));
    }
    @Test(priority = 2, dependsOnMethods = "testCreateAndModifyBooking", groups = {"regression"})
    @Story("ჯავშნის წაშლა")
    @Description("ეს ტესტი ამოწმებს ჯავშნის წარმატებულ წაშლას (DELETE) და შემდეგ ამოწმებს, რომ ჯავშანი აღარ არსებობს (404).")
    public void testDeleteBooking() {
        Response deleteResponse = bookingSteps.deleteBooking(bookingId, token);
        deleteResponse.then().statusCode(201);

        Response getResponse = bookingSteps.getBooking(bookingId);
        getResponse.then().statusCode(404);
    }

    @Test(priority = 3, groups = {"regression"})
    @Story("ნეგატიური სცენარები")
    @Description("ეს ტესტი ამოწმებს სისტემის რეაგირებას არასწორი/არარსებული ჯავშნის ID-ის მოთხოვნისას.")
    public void testGetNonExistingBookingNegative() {
        int fakeBookingId = 999999;

        Response getResponse = bookingSteps.getBooking(fakeBookingId);
        getResponse.then().statusCode(404);
    }
}