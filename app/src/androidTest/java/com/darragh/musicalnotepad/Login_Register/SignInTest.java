package com.darragh.musicalnotepad.Login_Register;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.darragh.musicalnotepad.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class SignInTest {
    private String email,password,username;


    @Rule
    public ActivityTestRule<SignIn> mActivityRule = new ActivityTestRule<>(
            SignIn.class);

    @Before
    public void initValidString() {
        email = "testUser11@gmail.com";
        username = "testUser1";
        password = "1234oclockrock";
    }

    @Test
    public void registerUser(){
        onView(withId(R.id.signInEmail))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.signInPassword))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.buttonSignIn)).perform(click());
    }

}