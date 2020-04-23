package org.dplatform.sdk;

public class PayModel extends DataModel {

    public PayModel() {
        super("pay");
    }

    public void setToken(String token) {
        put("token", NullCheck.nonNull(token, "token is null!"));
    }

    public void setOrderSn(String orderSn) {
        put("orderSn", NullCheck.nonNull(orderSn, "orderSn is null!"));
    }
}
