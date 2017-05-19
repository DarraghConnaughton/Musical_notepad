package com.darragh.musicalnotepad;

import android.support.test.annotation.UiThreadTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.darragh.musicalnotepad.Login_Register.SignIn;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SignInTest{

    @Rule
    public ActivityTestRule<SignIn>mActivityTestRule = new ActivityTestRule<SignIn>(SignIn.class);

    private SignIn mSignIn = null;

    @Before
    public void setUp() throws Exception {
        mSignIn = mActivityTestRule.getActivity();
    }

    @Test
    public void emailValidation(){

    }


    @UiThreadTest
    public void testLaunch(){
        View view = mSignIn.findViewById(R.id.signInEmail);
        EditText password = (EditText) mSignIn.findViewById(R.id.signInPassword);
        EditText email = (EditText) mSignIn.findViewById(R.id.signInEmail);
        Button button = (Button) mSignIn.findViewById(R.id.buttonSignIn);
        email.setText("Darragh.C@gmail.com");
        password.setText("test12345");
        button.performClick();
        
    }

    @After
    public void tearDown() throws Exception {

        mSignIn = null;

    }

}