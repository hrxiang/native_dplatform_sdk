package org.dplatform.sdk;

public interface Constant {
    String OTHER_CALLBACK_SCHEME = "dplatform.data.callback.%s";
    String DEFAULT_CALLBACK_SCHEME = "org.dplatform.game.%s.%s";
    String PLATFORM_SCHEME = "org.dplatform.lite.%s://do";
    String PLATFORM_PACKAGE_NAME = "com.dplatform.lite.%s";
    //联调
    String PLATFORM_APP_DOWNLOAD_URL_DEBUG = "http://139.9.201.236/appdownload/%s";
    //测试
    String PLATFORM_APP_DOWNLOAD_URL_TEST = "http://139.9.184.9/appdownload/%s";
    //预发
    String PLATFORM_APP_DOWNLOAD_URL_PRO = "https://dpl-pre.dpay.tech/appdownload/%s";
    //生产
    String PLATFORM_APP_DOWNLOAD_URL_RELEASE = "https://www.dpay.tech/appdownload/%s";


    String URL_TEST = "http://139.9.184.9/gateway/"; //测试
    String URL_DEBUG = "http://139.9.201.236/gateway/"; //联调环境
    String URL_PPO = "https://dpl-pre.winplus.top/gateway/"; //预发
    String URL_RELEASE = "https://www.winplus.top/gateway/"; //生产

    interface Key {
        String PAY = "pay";
        String TOKEN = "token";
        String ORDER_SN = "orderSn";
        String UUID = "outUid";
        String CHANNEL_NO = "channelNo";
    }
}
