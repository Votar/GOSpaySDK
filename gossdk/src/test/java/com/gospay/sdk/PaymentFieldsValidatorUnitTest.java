package com.gospay.sdk;

import com.gospay.sdk.util.PaymentFieldsValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.assertTrue;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class PaymentFieldsValidatorUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup(){
    }
    @Test
    public void testPriceValidator() throws Exception {

        assertTrue("Price should be valid", PaymentFieldsValidator.isPriceValid(12.2f));
        assertTrue("Price should be valid", PaymentFieldsValidator.isPriceValid(0f));
        assertTrue("Price should be valid", PaymentFieldsValidator.isPriceValid(1222.2f));
        assertTrue("Price should be valid", PaymentFieldsValidator.isPriceValid(0.4f));
    }

    @Test
    public void testDescriptionValidator() throws Exception {}

    @Test
    public void testCurrencyValidator() throws Exception {}
    @Test
    public void testOrderIdValidator() throws Exception {}

}