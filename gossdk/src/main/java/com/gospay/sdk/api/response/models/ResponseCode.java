package com.gospay.sdk.api.response.models;

/**
 * Created by bertalt on 01.09.16.
 */
public abstract class ResponseCode {

    public static final int ERROR_VALIDATION=400;
    public static final int ERROR_ACCESS_FORBIDDEN=403;
    public static final int ERROR_TOKEN_NOT_FOUND = 404;
    public static final int UNKNOWN_CODE =0;

    public class AddCardResponseCode {

        public static final int OK_WITH_EXIST_TOKEN=200;
        public static final int OK_WITHOUT_TOKEN=201;

    }

    public class GetCardListResponseCode {

        public static final int OK_WITH_ARRAY = 200;
    }

}
