package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Story;
import org.testng.annotations.Test;
import steps.AuthSteps;
import steps.BookingSteps;
import utils.BaseTest;

public class BookingTests extends BaseTest {
    AuthSteps authSteps = new AuthSteps();
    BookingSteps bookingSteps = new BookingSteps();

    @Test(priority = 1, groups = {"Regression"})
    @Story("ბუკინგის მართვა")
    @Description("ბუკინგის შექმნა და ვალიდაცია")
    public void firstTest() {
        // აქ მარიამი დაწერს ლოგიკას
    }
}