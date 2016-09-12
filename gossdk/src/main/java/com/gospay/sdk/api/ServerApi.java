package com.gospay.sdk.api;

import com.google.gson.JsonElement;
import com.gospay.sdk.api.request.models.card.CardFields;
import com.gospay.sdk.api.response.models.GosResponse;


/**
 * Created by bertalt on 30.08.16.
 */
public interface ServerApi {

    String BACKEND_URL = "https://gos.dev.perfsys.com:7443/gateway/";

    String HEADER_LOCALE = "Locale";
    String HEADER_COOKIE = "Cookie:GOS.TRACK=SUDLXB6DME5TIHPKTY2CMEP57GC72UDXOFD3PESDPV6AMP5Z2PVTMUP5BQQTC7LAYJKXNUKIEIIJ2K74G3A3FC6RKNV5IVMLUA5TYDVB7OTAGO6B4VOTRGE5P4AF6JFL2WR4FAGI7XYZXMJHJHORPAE";
    String HEADER_CONTENT_TYPE = "";
    String HEADER_API_KEY = "ApiKey";

    interface GOS_HEADERS {
        String CONTENT_TYPE = "Content-type";
        String LOCALE = "Locale";
        String API_KEY = "ApiKey";
        //Temporary
        String COOKIE = "Cookie";
    }

    interface GOS_METHODS {
        String POST = "POST";
        String GET = "GET";
    }
    interface GOS_REQUESTS{
        String ADD_CARD = "cards/add";
        String GET_CARD_LIST = "cards/list";
    }

}
