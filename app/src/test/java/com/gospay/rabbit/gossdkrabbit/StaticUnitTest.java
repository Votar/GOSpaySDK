package com.gospay.rabbit.gossdkrabbit;

import com.gospay.sdk.util.CreditCardValidator;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class StaticUnitTest {

    @Before
    public void setup(){
    }
    @Test

    public void testCardValidator() throws Exception {

        assertTrue("Card should be valid",CreditCardValidator.isCardValid("4111111111111111"));
        assertFalse("Card should be invalid",CreditCardValidator.isCardValid("1111111111111111"));

    }


}