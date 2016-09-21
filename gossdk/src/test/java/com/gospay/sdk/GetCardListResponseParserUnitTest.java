package com.gospay.sdk;

import org.junit.Before;
import org.junit.Test;

/**
 * Created by bertalt on 07.09.16.
 */
public class GetCardListResponseParserUnitTest {

    private String validArray =
            " [\n" +
            "      {\n" +
            "         \"cardMask\":\"4111********1111\",\n" +
            "         \"cardName\":\"Corporate_card\",\n" +
            "          \"cardId\":\"550e8400-e29b-41d4-a716-446655440000\",\n" +
            "         \"expireDate\":\"0119\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"cardMask\":\"5167********9699\",\n" +
            "         \"cardName\":\"Corporate_card\",\n" +
            "\"cardId\":\"550e8400-e29b-41d4-a716-446655440000\",\n" +
            "         \"expireDate\":\"0217\"\n" +
            "      }\n" +
            "   ]\n";

    @Before
    public void setup(){
        validArray = validArray.replace(" ","").replace("\n","");
    }
    @Test
    public void testParserOkResponse() throws Exception{

    }
}
