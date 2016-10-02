package com.gospay.sdk.api;

/**
 * Created by bertalt on 30.08.16.
 */
public interface GosServerApi {

    String BACKEND_URL = "https://gos.dev.perfsys.com:7443/gateway/";

    interface GOS_HEADERS {
        String CONTENT_TYPE = "Content-type";
        String LOCALE = "Accept-Language";
        String API_KEY = "ApiKey";
        //Temporary
        String COOKIE = "Cookie";
        String ORIGIN = "Origin";
    }

    interface GOS_METHODS {
        String POST = "POST";
        String GET = "GET";
    }
    interface GOS_REQUESTS{
        String ADD_CARD = "cards/add";
        String GET_CARD_LIST = "cards/list";
        String INIT_PAYMENT = "payment";
        String CONFIRM_PAYMENT = "payment/confirm";
        String GET_PAYMENT_STATUS = "payment/status";
        String REMOVE_CARD = "cards/remove";
        String INIT_PAYMENT_WITH_CARD = "payWithCard";
    }
}
