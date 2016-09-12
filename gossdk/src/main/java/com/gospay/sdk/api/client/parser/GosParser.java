package com.gospay.sdk.api.client.parser;

import com.google.gson.stream.JsonReader;
import com.gospay.sdk.api.response.models.GosResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by bertalt on 11.09.16.
 */
public class GosParser {


    private GosResponse parseResponse(InputStream in) throws IOException {

        JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
        try {
            int resultCode = -1;
            reader.beginObject();
            /*while (reader.hasNext()) {
                String name = reader.nextName();
                if(name.equalsIgnoreCase(GosResponse.JSONFields.RESULT_CODE))
                    resultCode = reader.nextInt();
            }*/
        } finally {
            reader.close();
        }

        return null;
    }
}
