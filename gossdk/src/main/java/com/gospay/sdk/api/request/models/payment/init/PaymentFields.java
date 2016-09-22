package com.gospay.sdk.api.request.models.payment.init;

import com.gospay.sdk.exceptions.GosInvalidPaymentFieldsException;
import com.gospay.sdk.util.PaymentFieldsValidator;

import java.util.Currency;

/**
 * Created by bertalt on 08.09.16.
 */
public class PaymentFields {

    private double price;
    private Currency currency;
    private String description;
    private String order;

    public static PaymentFields create(double amount, String currency, String description, String order) throws GosInvalidPaymentFieldsException{

        if(!PaymentFieldsValidator.isPriceValid(amount))
                throw new GosInvalidPaymentFieldsException(String.format("Invalid price %1f", amount), GosInvalidPaymentFieldsException.GosPaymentField.PRICE);

        if(!PaymentFieldsValidator.isCurrencyValid(currency))
            throw new GosInvalidPaymentFieldsException(String.format("Invalid currency %1s", currency), GosInvalidPaymentFieldsException.GosPaymentField.CURRENCY);

        if(!PaymentFieldsValidator.isOrderIdValid(order))
            throw new GosInvalidPaymentFieldsException(String.format("Invalid orderId %1s", order), GosInvalidPaymentFieldsException.GosPaymentField.ORDER_ID);

        if(!PaymentFieldsValidator.isDescriptionValid(description))
            throw new GosInvalidPaymentFieldsException(String.format("Invalid description %1s", description), GosInvalidPaymentFieldsException.GosPaymentField.DESCRIPTION);

        return new PaymentFields(amount, currency, description, order);
    }

    private PaymentFields(double amount, String currency, String description, String order) {
        this.price = amount;
        this.currency = Currency.getInstance(currency);
        this.description = description;
        this.order = order;
    }

    public double getPrice() {
        return price;
    }

    public Currency getCurrency() {
        return currency;
    }

    public String getDescription() {
        return description;
    }

    public String getOrder() {
        return order;
    }
}
