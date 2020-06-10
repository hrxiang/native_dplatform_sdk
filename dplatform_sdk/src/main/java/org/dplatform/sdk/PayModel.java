package org.dplatform.sdk;

public class PayModel extends DataModel {

    public PayModel() {
        super(Constant.Key.PAY);
    }

    public void setToken(String token) {
        setAttrValue(Constant.Key.TOKEN, NullCheck.nonNull(token, "token is null!"));
    }

    public void setOrderSn(String orderSn) {
        setAttrValue(Constant.Key.ORDER_SN, NullCheck.nonNull(orderSn, "orderSn is null!"));
    }

    public void setUuid(String uuid) {
        setAttrValue(Constant.Key.UUID, NullCheck.nonNull(uuid, "uuid is null!"));
    }

    public void setChannelNo(String channelNo) {
        setAttrValue(Constant.Key.CHANNEL_NO, NullCheck.nonNull(channelNo, "channelNo is null!"));
    }

    public void setAttrValue(String attrKey, Object attrValue) {
        put(attrKey, attrValue);
    }
}
