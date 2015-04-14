package com.globallogic.zoo;

import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.test.ActivityInstrumentationTestCase2;

import com.globallogic.zoo.activities.LoginActivity;
import com.globallogic.zoo.activities.WelcomeActivity;

import org.junit.Before;

import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.anyOf;;

import static android.support.test.espresso.action.ViewActions.typeText;


/**
 * Created by rodrigo on 4/6/15.
 */
public class WelcomeActivityTest extends ActivityInstrumentationTestCase2 {
    private final String GOODUSER = "GL";
    private final String GOODPASS = "Android";

    public WelcomeActivityTest() {
        super(LoginActivity.class);
    }

    @Before
    public void setUp() throws Exception {
        super.setUp();
        getActivity();

        onView(withId(R.id.mainactivity_user)).
                perform(typeText(GOODUSER));
        onView(withId(R.id.mainactivity_pass)).
                perform(typeText(GOODPASS));
        onView(withId(R.id.mainactivity_siging)).perform(scrollTo(), click());
    }

    public void testShouldShowUserName() {
        onView(withId(R.id.welcomeactivity_welcome)).check(matches(isDisplayed()));
    }

    public void testShouldAllowLogOut() {
        onView(withId(R.id.welcomeactivity_signout)).perform(click()).check(doesNotExist());
    }

    public void testShouldShowAListOfAnimals() {
        onView(withId(R.id.welcomeactivity_recycleview)).check(matches(isDisplayed()));
    }

    public void testAnAnimalShouldShowANameAPhotoAndASpecie() {
    }
 }
