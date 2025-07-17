package com.test.hft.presentation.exception;

/**
 * Author: Vuxie
 * Date : 17/07/2025
 */
public enum ErrorCode {
    ORDER_NOT_FOUND("ERR_404", "error.order.not_found"),
    INVALID_ORDER_STATUS("ERR_400", "error.order.invalid_status"),
    CANNOT_CANCEL_ORDER("ERR_400", "error.order.cannot_cancel"),
    INTERNAL_ERROR("ERR_500", "error.internal");

    private final String code;
    private final String messageKey;

    ErrorCode(String code, String messageKey) {
        this.code = code;
        this.messageKey = messageKey;
    }

    public String getCode() {
        return code;
    }

    public String getMessageKey() {
        return messageKey;
    }
}