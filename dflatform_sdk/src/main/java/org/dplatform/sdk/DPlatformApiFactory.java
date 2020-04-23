package org.dplatform.sdk;

import android.app.Activity;

import java.lang.ref.WeakReference;


public class DPlatformApiFactory {
    private DPlatformApiFactory() {
    }


    static DPlatformApi api;

    /**
     * @param activity 上下文
     * @param site     站点
     * @param scheme   当前app注册的scheme
     * @param evn      平台环境
     * @return DPlatformApi
     */
    public static DPlatformApi createApi(Activity activity, String site, String scheme, DPlatformEvn evn) {
        return api = new DPlatformApi(new WeakReference<>(NullCheck.nonNull(activity, "activity is null!")), NullCheck.nonNull(site, "site is null!"), scheme, activity.getPackageName(), evn);
    }
}
