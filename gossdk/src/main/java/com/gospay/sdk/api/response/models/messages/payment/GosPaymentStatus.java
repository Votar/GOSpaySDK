package com.gospay.sdk.api.response.models.messages.payment;

/**
 * Created by bertalt on 08.09.16.
 */
public interface GosPaymentStatus {
    String PENDING = "PENDING";
    String QUEUED = "QUEUED";
    String AUTHORIZATION = "AUTHORIZATION";
    String PROCESSING = "PROCESSING";
    String DECLINED = "DECLINED";
    String ERROR = "ERROR";
    String CANCELED = "CANCELED";
    String VERIFICATION_3D_SECURE_REQUIRED = "VERIFICATION_3D_SECURE_REQUIRED";
    String APPROVED = "APPROVED";
}
