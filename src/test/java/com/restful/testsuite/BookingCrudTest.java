package com.restful.testsuite;


import com.restful.bookinginfo.BookingStep;
import com.restful.model.BookingPojo;
import com.restful.testbase.TestBase;
import com.restful.utils.TestUtils;
import io.restassured.response.ValidatableResponse;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Steps;
import net.thucydides.core.annotations.Title;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.HashMap;


import static org.hamcrest.Matchers.hasValue;
@RunWith(SerenityRunner.class)
public class BookingCrudTest extends TestBase {
    static String firstname = "Mann" + TestUtils.getRandomValue();
    static String lastname = "Patel" + TestUtils.getRandomValue();
    static int totalprice = Integer.parseInt(1 + TestUtils.getRandomValue());
    static boolean depositpaid = true;
    static String additionalneeds = "Vegetarian";

    static String token;
    static int id;

    @Steps
    BookingStep bookingStep;

    @Title("This method will create a Tokken")
    @org.junit.Test
    public void test001() {
        ValidatableResponse response =bookingStep.getTokken().statusCode(200);
        token = response.extract().path("token");
    }

    @Title("This method will create a booking")
    @org.junit.Test
    public void test002() {
        BookingPojo.BookingDates bookingdates = new BookingPojo.BookingDates();
        bookingdates.setCheckin("2022-10-01");
        bookingdates.setCheckout("2022-12-01");
        ValidatableResponse response = bookingStep.createBooking(firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds).statusCode(200);
        id = response.extract().path("bookingid");
    }

    @Title("This method will verify new Booking ID creation")
    @Test
    public void test003() {
        ValidatableResponse response = bookingStep.getBookingInfoByID();
        ArrayList<?> booking = response.extract().path("bookingid");
        Assert.assertTrue(booking.contains(id));
    }

    @Title("This method will get booking with Id")
    @org.junit.Test
    public void test004() {
        bookingStep.getSingleBookingIDs(id).statusCode(200);
    }

    @Title("This method will updated a booking with ID")
    @Test
    public void test005() {
        additionalneeds = "none";
        BookingPojo.BookingDates bookingdates = new BookingPojo.BookingDates();
        bookingdates.setCheckin("2022-10-01");
        bookingdates.setCheckout("2022-12-01");
        bookingStep.updateBookingWithID(id, token, firstname, lastname, totalprice, depositpaid, bookingdates, additionalneeds);
        ValidatableResponse response = bookingStep.getSingleBookingIDs(id);
        HashMap<String, ?> update = response.extract().path("");
        Assert.assertThat(update, hasValue("none"));
    }

    @Title("This method will delete a booking with ID")
    @Test
    public void test006() {
        bookingStep.deleteABookingID(id, token).statusCode(201);
        bookingStep.getSingleBookingIDs(id).statusCode(404);
    }
}

