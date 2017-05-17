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
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RegisterTest {
    private String email,password,username;


    @Rule
    public ActivityTestRule<Register> mActivityRule = new ActivityTestRule<>(
            Register.class);

    @Before
    public void initValidString() {
        // Specify a valid string.
        email = "testUser11@gmail.com";
        username = "testUser1";
        password = "1234oclockrock";
    }

    @Test
    public void registerUser(){
        onView(withId(R.id.editEmail))
                .perform(typeText(email), closeSoftKeyboard());
        onView(withId(R.id.password))
                .perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.userName))
                .perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.buttonRegister)).perform(click());
    }

}