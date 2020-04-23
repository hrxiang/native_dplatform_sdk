package org.dplatform.sdk;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

public class DPlatformApiCallbackActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // fixing portrait mode problem for SDK 26 if using windowIsTranslucent = true
        if (Build.VERSION.SDK_INT != Build.VERSION_CODES.O) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

        super.onCreate(savedInstanceState);
        System.out.println("==============DPlatformApiCallbackActivity start=================");

        if (!createdApi()) return;

        if (enabledCallback()) {
            DPlatformApiFactory.api.parseIntent(getIntent(), new OnUriIsNullListener() {
                @Override
                public void onUriIsNull() {
                    try {
                        if (!Utils.isInstalled(DPlatformApiCallbackActivity.this, getPlatformPackageName())) {
                            Utils.openUri(DPlatformApiCallbackActivity.this, getPlatformDownloadUrl());
                        } else {
                            Uri uri = getPlatformUri();
                            if (null != uri) {
                                Utils.call(DPlatformApiCallbackActivity.this, uri, new WithParameter() {
                                    @Override
                                    public void with(Intent intent) {
                                        intent.setAction(Intent.ACTION_VIEW);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    }
                                });
                            }
                        }
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(DPlatformApiCallbackActivity.this, "未找到注册scheme的钱包", Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
        //
        finish();
    }

    private static boolean createdApi() {
        return null != DPlatformApiFactory.api;
    }

    private static boolean enabledCallback() {
        return createdApi() && null != DPlatformApiFactory.api.getCallback();
    }

    private static String getPlatformPackageName() {
        return DPlatformApiFactory.api.getPlatformPackageName();
    }

    private static String getPlatformDownloadUrl() {
        return DPlatformApiFactory.api.getPlatformDownloadUrl();
    }

    private static Uri getPlatformUri() {
        return DPlatformApiFactory.api.buildPlatformUri();
    }
}
