package com.example.norman_lee.myapplication;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {

    @Rule
    public ActivityTestRule mActivityRule = new ActivityTestRule<>(
            MainActivity.class);


    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();

        assertEquals("com.example.norman_lee.myapplication", appContext.getPackageName());

    }

    //TODO 5.5 check that the layout of MainActivity has the required widgets


    /* TODO 5.6 For 5 units of A buys 1 unit of B, enter these values in SubActivity and
       TODO 5.6 check that the result 0.2 is displayed in Main activity */


    /* TODO 5.7 If 0 is entered in the editTextSubValueA in SubActivity
       TODO 5.7 check that textViewExchangeRate is not displayed when button is clicked
       TODO 5.7 this is a proxy for checking that the explicit intent is not carried out
     */


    //TODO 5.8 Write a class called CheckEditText to check that an editText widget is numberInput
    //TODO 5.8 Check that the EditText Widgets in this app accept numerical inputs only


    /* TODO 5.9 [On your own] check that if the EditText widgets in SubActivity are blank
       TODO 5.9 and button is clicked, MainActivity is not displayed (See TODO 5.6)
     */



}

