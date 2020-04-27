package org.dplatform.sdk;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.dplatform.sdk.Constant.CALLBACK_SCHEME;
import static org.dplatform.sdk.Constant.DEFAULT_CALLBACK_SCHEME;
import static org.dplatform.sdk.Constant.PLATFORM_APP_DOWNLOAD_URL_PRO;
import static org.dplatform.sdk.Constant.PLATFORM_APP_DOWNLOAD_URL_RELEASE;
import static org.dplatform.sdk.Constant.PLATFORM_APP_DOWNLOAD_URL_TEST;
import static org.dplatform.sdk.Constant.PLATFORM_PACKAGE_NAME;
import static org.dplatform.sdk.Constant.PLATFORM_SCHEME;

public final class DPlatformApi {
    private WeakReference<Activity> reference;
    private Uri.Builder builder;
    private String site;
    private String packageName;
    private Map<String, Object> params = new HashMap<>();
    private DPlatformApiCallback callback;
    private OnCheckReqUriListener onCheckReqUriListener;
    private DPlatformEvn evn;

    DPlatformApi(WeakReference<Activity> reference, String site, String packageName, DPlatformEvn evn) {
        this.reference = reference;
        this.site = site.toLowerCase();
        this.evn = evn;
        this.packageName = packageName;
        this.builder = newUriBuilder();
    }

    public void putParameter(String key, Object value) {
        params.put(key, value);
    }

    public void putModel(DataModel model) {
        if (null != model) {
            params.putAll(model.getMap());
        }
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

    public void site(String site) {
        this.site = NullCheck.nonNull(site, "site is null").toLowerCase();
    }

    String getPlatformPackageName() {
        return String.format(PLATFORM_PACKAGE_NAME, site);
    }

    String getPlatformDownloadUrl() {
        if (DPlatformEvn.TEST.equals(evn)) {
            return String.format(PLATFORM_APP_DOWNLOAD_URL_TEST, site);
        } else if (DPlatformEvn.PRO.equals(evn)) {
            return String.format(PLATFORM_APP_DOWNLOAD_URL_PRO, site);
        } else {
            return String.format(PLATFORM_APP_DOWNLOAD_URL_RELEASE, site);
        }
    }

    private String getPlatformScheme() {
        return String.format(PLATFORM_SCHEME, site);
    }

    private String getCallbackScheme() {
        return String.format(CALLBACK_SCHEME, packageName);
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
        Map<String, String> arguments = new HashMap<>();
        if (null != intent) {
            Uri uri = intent.getData();
            System.out.println("============DPlatformCustomApi parse uri==============" + uri);
            if (null != uri) {
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
                if (null != callback) callback.onResult(new JSONObject(arguments));
            } else {
                if (null != listener) listener.onUriIsNull();
            }
        }
    }

    Uri buildPlatformUri() {
        return builder
                .clearQuery()
                .appendQueryParameter("packageName", packageName)
                .appendQueryParameter("callbackScheme", getCallbackScheme())
                .appendQueryParameter("params", buildJsonStrParams())
                .build();
    }

    private String buildJsonStrParams() {
        return new JSONObject(checkRequiredParams(params)).toString();
    }

    private Uri.Builder newUriBuilder() {
        return Uri.parse(getPlatformScheme()).buildUpon();
    }

    private Map<String, Object> checkRequiredParams(Map<String, Object> map) {
        if (null == map.get("scheme")) {
            map.put("scheme", getDefaultCallbackScheme());
        }
        NullCheck.nonNull(map.get("action"), "action is null!");
        return map;
    }
}

