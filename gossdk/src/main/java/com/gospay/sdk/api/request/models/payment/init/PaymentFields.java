package com.gospay.sdk.api.request.models.payment.init;



import com.gospay.sdk.exceptions.GosInvalidPaymentFieldsException;
import com.gospay.sdk.util.PaymentFieldsValidator;

import java.util.Currency;

/**
 * Created by bertalt on 08.09.16.
 * Model to serialization payment description to GSON
 */
public class PaymentFields {

    private double price;
    private Currency currency;
    private String description;
    private String orderId;

    /**
     *
     * @param amount value to payment
     * @param currency In orderId ISO 4217. For example "USD" or "UAH"
     * @param description Short comment about payment
     * @param orderId Unique value of your orderId for statistic
     * @return
     * @throws GosInvalidPaymentFieldsException
     */
    public static PaymentFields create(double amount, String currency, String description, String orderId) throws GosInvalidPaymentFieldsException{

        if(!PaymentFieldsValidator.isPriceValid(amount))
                throw new GosInvalidPaymentFieldsException(String.format("Invalid price %1f", amount), GosInvalidPaymentFieldsException.GosPaymentField.PRICE);

        if(!PaymentFieldsValidator.isCurrencyValid(currency))
            throw new GosInvalidPaymentFieldsException(String.format("Invalid currency %1s", currency), GosInvalidPaymentFieldsException.GosPaymentField.CURRENCY);

        if(!PaymentFieldsValidator.isOrderIdValid(orderId))
            throw new GosInvalidPaymentFieldsException(String.format("Invalid orderId %1s", orderId), GosInvalidPaymentFieldsException.GosPaymentField.ORDER_ID);

        if(!PaymentFieldsValidator.isDescriptionValid(description))
            throw new GosInvalidPaymentFieldsException(String.format("Invalid description %1s", description), GosInvalidPaymentFieldsException.GosPaymentField.DESCRIPTION);

        return new PaymentFields(amount, currency, description, orderId);
    }

    private PaymentFields(double amount, String currency, String description, String order) {
        this.price = amount;
        this.currency = Currency.getInstance(currency);
        this.description = description;
        this.orderId = order;
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
        return orderId;
    }
}
