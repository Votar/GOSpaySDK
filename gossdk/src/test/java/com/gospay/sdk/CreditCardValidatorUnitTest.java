package com.gospay.sdk;

import com.gospay.sdk.api.util.ExpireDateModel;
import com.gospay.sdk.exceptions.GosSdkException;
import com.gospay.sdk.util.CreditCardValidator;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class CreditCardValidatorUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void setup(){
    }
    @Test
    public void testCardNumberValidator() throws Exception {

        assertTrue("Card should be valid", CreditCardValidator.isCardValid("4111111111111111"));
        assertFalse("Card should be invalid",CreditCardValidator.isCardValid("1111111111111111"));

    }
    @Test
    public void testExpireDateValidator() throws Exception {

        assertTrue("Expire date should be valid", CreditCardValidator.isExpireDateValid("01","19"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("02","14"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid(null,"14"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("02",null));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("13","14"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("13","17"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("111","14"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("","14"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("02",""));
        assertTrue("Expire date should be invalid", CreditCardValidator.isExpireDateValid("9","16"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("8","16"));
        assertFalse("Expire date should be invalid", CreditCardValidator.isExpireDateValid("02","14"));

    }
    @Test
    public void testCvvValidator() throws Exception {

        assertFalse("Cvv should be invalid", CreditCardValidator.isCvvValid("1"));
        assertFalse("Cvv should be invalid", CreditCardValidator.isCvvValid(null));
        assertFalse("Cvv should be invalid", CreditCardValidator.isCvvValid("000"));
        assertFalse("Cvv should be invalid", CreditCardValidator.isCvvValid("0001"));
        assertFalse("Cvv should be invalid", CreditCardValidator.isCvvValid("1000"));
        assertFalse("Cvv should be invalid", CreditCardValidator.isCvvValid("13fe"));
        assertFalse("Cvv should be invalid", CreditCardValidator.isCvvValid("-12"));
        assertFalse("Cvv should be invalid", CreditCardValidator.isCvvValid("-122"));
    }

    @Test
    public void testExpireDateModel() throws Exception{

        assertNotNull(ExpireDateModel.create("0119"));
        assertNotNull(ExpireDateModel.create("0916"));
        thrown.expect(GosSdkException.class);
        assertNull(ExpireDateModel.create(null));
        thrown.expect(GosSdkException.class);
        assertNull(ExpireDateModel.create("1319"));
        thrown.expect(GosSdkException.class);
        assertNull(ExpireDateModel.create("fdst"));
    }

}