package org.dplatform.sdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.dplatform.sdk.Constant.DEFAULT_CALLBACK_SCHEME;
import static org.dplatform.sdk.Constant.OTHER_CALLBACK_SCHEME;
import static org.dplatform.sdk.Constant.PLATFORM_APP_DOWNLOAD_URL_DEBUG;
import static org.dplatform.sdk.Constant.PLATFORM_APP_DOWNLOAD_URL_PRO;
import static org.dplatform.sdk.Constant.PLATFORM_APP_DOWNLOAD_URL_RELEASE;
import static org.dplatform.sdk.Constant.PLATFORM_APP_DOWNLOAD_URL_TEST;
import static org.dplatform.sdk.Constant.PLATFORM_PACKAGE_NAME;
import static org.dplatform.sdk.Constant.PLATFORM_SCHEME;

public final class DPlatformApi {
    private WeakReference<Activity> reference;
    private String site;
    private String customPlatformPackageName;
    private String customPlatformScheme;
    private String customPlatformDownloadUrl;
    private String customCurrentScheme;
    private String packageName;
    private final Map<String, Object> params = new HashMap<>();
    private DPlatformApiCallback callback;
    private OnCheckReqUriListener onCheckReqUriListener;
    private DPlatformEvn evn;

    DPlatformApi(WeakReference<Activity> reference, String site, String packageName, DPlatformEvn evn) {
        this.reference = reference;
        this.site = site.toLowerCase();
        this.evn = evn;
        this.packageName = packageName;
    }

    public void putParameter(String key, Object value) {
        params.put(key, value);
    }

    public void putModel(DataModel model) {
        if (null != model) {
            params.putAll(model.getMap());
        }
    }

    public Map<String, Object> getParameters() {
        return params;
    }

    public void setCallback(DPlatformApiCallback callback) {
        this.callback = callback;
        System.out.println("==========DPlatformCustomApi onCreated==========");
        parseIntent(getIntent(), null);
    }

    public void sendReq() {
        Activity ac = getActivity();
        if (null != ac) {
            Intent i = new Intent(ac, DPlatformApiCallbackActivity.class);
            ac.startActivity(i);
        }
    }

    public void customPlatformPackageName(String packageName) {
        this.customPlatformPackageName = packageName;
    }

    public void customPlatformScheme(String scheme) {
        this.customPlatformScheme = scheme;
    }

    public void customPlatformDownloadUrl(String downloadUrl) {
        this.customPlatformDownloadUrl = downloadUrl;
    }

    public void customCurrentScheme(String scheme) {
        this.customCurrentScheme = scheme;
    }

    String getPlatformPackageName() {
        if (null != customPlatformPackageName) return customPlatformPackageName;
        return String.format(PLATFORM_PACKAGE_NAME, site);
    }

    private String getPlatformScheme() {
        if (null != customPlatformScheme) return customPlatformScheme;
        return String.format(PLATFORM_SCHEME, site);
    }

    String getPlatformDownloadUrl() {
        if (null != customPlatformDownloadUrl) return customPlatformDownloadUrl;
        if (DPlatformEvn.DEBUG.equals(evn)) {
            return String.format(PLATFORM_APP_DOWNLOAD_URL_DEBUG, site);
        } else if (DPlatformEvn.TEST.equals(evn)) {
            return String.format(PLATFORM_APP_DOWNLOAD_URL_TEST, site);
        } else if (DPlatformEvn.PRO.equals(evn)) {
            return String.format(PLATFORM_APP_DOWNLOAD_URL_PRO, site);
        } else {
            return String.format(PLATFORM_APP_DOWNLOAD_URL_RELEASE, site);
        }
    }

    private String getOtherCallbackScheme() {
        return String.format(OTHER_CALLBACK_SCHEME, packageName);
    }

    private String getDefaultCallbackScheme() {
        return String.format(DEFAULT_CALLBACK_SCHEME, site, packageName);
    }

    private Activity getActivity() {
        return reference.get();
    }

    private Intent getIntent() {
        if (null != getActivity()) return getActivity().getIntent();
        return null;
    }

    public void onNewIntent(Intent intent) {
        System.out.println("==========DPlatformCustomApi onNewIntent==========");
        parseIntent(intent, null);
    }

    DPlatformApiCallback getCallback() {
        return callback;
    }

    OnCheckReqUriListener getCheckReqUriListener() {
        return onCheckReqUriListener;
    }

    public void setCheckReqUriListener(OnCheckReqUriListener onCheckReqUriListener) {
        this.onCheckReqUriListener = onCheckReqUriListener;
    }

    void parseIntent(Intent intent, OnUriIsNullListener listener) {
        try {
            Map<String, String> arguments = new HashMap<>();
            if (null != intent) {
                Uri uri = intent.getData();
                if (null != uri) {
                    System.out.println("============DPlatformCustomApi parse uri==============" + Uri.decode(uri.toString()));
                    if (isValidScheme(uri)) {
                        if (null != callback) {
                            // 最新sdk获取数据的方式，最新sdk修复字段类型被修改的bug
                            String params = uri.getQueryParameter("commonSdkParams");
                            if (null != params) {
                                callback.onResult(new JSONObject(params));
                            } else {
                                //老版本lite获取数据的方式，存在字段类型被修改为string类型的bug
                                Set<String> keys = uri.getQueryParameterNames();
                                if (null != keys) {
                                    String value;
                                    for (String key : keys) {
                                        value = uri.getQueryParameter(key);
                                        if (null != value) {
                                            arguments.put(key, value);
                                        }
                                    }
                                }
                                callback.onResult(new JSONObject(arguments));
                            }
                        }
                    }
                } else {
                    if (null != listener) listener.onUriIsNull();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidScheme(Uri uri) {
        String receiverScheme = uri.getScheme();
        Object registerScheme = getCurrentRegisteredScheme();
        System.out.println("============收到的scheme:=======" + receiverScheme);
        System.out.println("============注册的scheme:=======" + registerScheme);
        return registerScheme.equals(receiverScheme);
    }

    private Object getCurrentRegisteredScheme() {
        return null == customCurrentScheme ? getDefaultCallbackScheme() : customCurrentScheme;
    }

    Uri buildPlatformUri() {
        return newUriBuilder()
                .clearQuery()
//                .appendQueryParameter("packageName", packageName)
//                .appendQueryParameter("otherScheme", getOtherCallbackScheme())
                .appendQueryParameter("params", buildJsonStrParams())
                .build();
    }

    private String buildJsonStrParams() {
        return new JSONObject(checkRequiredParams(params)).toString();
    }

    private Uri.Builder newUriBuilder() {
        String uri = getPlatformScheme();
        if (null != uri && !uri.contains("://")) {
            uri += "://do";
        }
        return Uri.parse(uri).buildUpon();
    }

    private Map<String, Object> checkRequiredParams(Map<String, Object> map) {
        map.put("scheme", getCurrentRegisteredScheme());
        map.put("otherScheme", getOtherCallbackScheme());
        map.put("packageName", packageName);
        return map;
    }
}

