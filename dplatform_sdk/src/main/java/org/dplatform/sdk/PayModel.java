package org.dplatform.sdk;

public class PayModel extends DataModel {

    public PayModel() {
        super("pay");
    }

    public void setToken(String token) {
        setAttrValue("token", NullCheck.nonNull(token, "token is null!"));
    }

    public void setOrderSn(String orderSn) {
        setAttrValue("orderSn", NullCheck.nonNull(orderSn, "orderSn is null!"));
    }

    public void setAttrValue(String attrKey, Object attrValue) {
        put(attrKey, attrValue);
    }
}
