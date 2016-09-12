package com.gospay.sdk;

import com.gospay.sdk.api.parsers.GetCardListParser;
import com.gospay.sdk.api.response.models.messages.card.CardView;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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

        ArrayList<CardView> list = GetCardListParser.parse(validArray);
        assertEquals(list.size(), 2);
        CardView cardView = list.get(0);
        assertEquals(cardView.getCardId(), "550e8400-e29b-41d4-a716-446655440000");
        assertEquals(cardView.getCardMask(), "4111********1111");
        assertEquals(cardView.getAlias(), "Corporate_card");


        assertNotNull(cardView.getCardId());
        assertNotNull(cardView.getCardMask());
        assertNotNull(cardView.getAlias());


    }
}