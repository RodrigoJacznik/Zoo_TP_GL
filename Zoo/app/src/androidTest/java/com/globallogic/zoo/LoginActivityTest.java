package com.globallogic.zoo;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.*;
import static android.support.test.espresso.action.ViewActions.*;

import android.support.test.espresso.ViewInteraction;
import android.test.ActivityInstrumentationTestCase2;
import android.test.suitebuilder.annotation.LargeTest;

import com.globallogic.zoo.activities.LoginActivity;


import org.junit.Before;

@LargeTest
public class LoginActivityTest extends ActivityInstrumentationTestCase2 {
    private final String GOODUSER = "GL";
    private final String GOODPASS = "Android";
    private final String BADUSER = "USER";
    private final String BADPASS = "PASS";

    private ViewInteraction signin;
    private ViewInteraction user;
    private ViewInteraction pass;
    private ViewInteraction error;
    private ViewInteraction welcome;

    public LoginActivityTest() {
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        getActivity();

        signin = onView(withId(R.id.mainactivity_siging));
        user = onView(withId(R.id.mainactivity_user));
        pass = onView(withId(R.id.mainactivity_pass));
        error = onView(withId(R.id.mainactivity_error));
        welcome = onView(withId(R.id.welcomeactivity_welcome));
    }

    public void testSignInShouldBeDisableIfEditTextAreBlank() {
        signin.check(matches(not(isEnabled())));
    }

    public void testErrorShouldNotBeVisible() {
        error.check(matches(not(isCompletelyDisplayed())));
    }

    public void testSignInShouldBeEnableIfEditTextHaveText() {
        user.perform(typeText(BADUSER), closeSoftKeyboard());
        signin.check(matches(not(isEnabled())));
        pass.perform(typeText(BADPASS), closeSoftKeyboard());
        signin.check(matches(isEnabled()));
    }

    public void testInvalidUserAndPassShouldShowAnErrorMessage() {
        user.perform(typeText(BADUSER), closeSoftKeyboard());
        pass.perform(typeText(BADPASS), closeSoftKeyboard());
        signin.perform(scrollTo(), click());

        error.check(matches(isDisplayed()));
    }

    public void testValidUserAndPassShouldStartWelcomeActivity() {
        user.perform(typeText(GOODUSER), closeSoftKeyboard());
        pass.perform(typeText(GOODPASS), closeSoftKeyboard());
        signin.perform(scrollTo(), click());

        welcome.check(matches(isDisplayed()));
    }
}