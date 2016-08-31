package com.gos.rabbit.gossdk.exceptions;

/**
 * Created by bertalt on 29.08.16.
 */
public class GosSdkException extends RuntimeException {

    public GosSdkException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
