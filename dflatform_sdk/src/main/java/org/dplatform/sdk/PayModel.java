package org.dplatform.sdk;

public class PayModel extends DataModel {
    private String token;
    private String orderSn;

    public PayModel(String token, String orderSn) {
        super("pay");
        this.token = NullCheck.nonNull(token, "token is null!");
        this.orderSn = NullCheck.nonNull(orderSn, "orderSn is null!");
    }

    public String getToken() {
        return token;
    }

    public String getOrderSn() {
        return orderSn;
    }
}
