package com.gospay.sdk;

import android.test.mock.MockContext;

import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.response.listeners.GosResponseListener;
import com.gospay.sdk.exceptions.GosSdkException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by bertalt on 07.09.16.
 */
public class AddCardParameterUnitTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();



    @Test
    public void AddCardFieldsTest() throws Exception{

        CardFields cardFields = CardFields.create(4111111111111111l,"01","19","144", null);
        thrown.expect(GosSdkException.class);
         CardFields.create(411111111111111l,"01","19","144", null);
        thrown.expect(GosSdkException.class);
        CardFields.create(4111111111111111l,"13","19","144", null);
        thrown.expect(GosSdkException.class);
        CardFields.create(411111111111111l,"01","15","144", null);
        thrown.expect(GosSdkException.class);
        CardFields.create(411111111111111l,"01","19","44", null);
        CardFields.create(411111111111111l,null,"19","144", null);
        thrown.expect(GosSdkException.class);
        CardFields.create(411111111111111l,"01",null,"144", null);
        thrown.expect(GosSdkException.class);
        CardFields.create(411111111111111l,"01","19",null, null);
        thrown.expect(GosSdkException.class);
        CardFields.create(0,"01","19","144", null);
        thrown.expect(GosSdkException.class);
        CardFields.create(411111111111111l,"01","19","1441", null);
        thrown.expect(GosSdkException.class);
        CardFields.create(411111111111111l,"01","19","144", null);
         //CardFields.create(4111111111111111l,"01","19","144", null);

    }
}
