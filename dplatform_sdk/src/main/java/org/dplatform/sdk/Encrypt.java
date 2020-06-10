package org.dplatform.sdk;

import android.app.Activity;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;
import static org.dplatform.sdk.Constant.URL_DEBUG;
import static org.dplatform.sdk.Constant.URL_PPO;
import static org.dplatform.sdk.Constant.URL_RELEASE;
import static org.dplatform.sdk.Constant.URL_TEST;

final class Encrypt {

    private static String post(Activity activity, String host, Map<String, Object> params, final DPlatformApiCallback apiCallback) {
        String result = null;
        final HttpURLConnection conn;
        try {

            String url = host + "user/app/userCenter/security/createToken";
//            url = "http://172.29.24.139:8081/user/app/userCenter/security/createToken";
            // 请求url
            URL postUrl = new URL(url);
            // 打开连接
            conn = (HttpURLConnection) postUrl.openConnection();
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setConnectTimeout(15_000);
            conn.setReadTimeout(15_000);
            conn.setRequestMethod("POST");    // 默认是 GET方式
            conn.setUseCaches(false);         // Post 请求不能使用缓存
            conn.setInstanceFollowRedirects(true);   //设置本次连接是否自动重定向
            //connection.setRequestProperty("User-Agent", "Mozilla/5.0");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.addRequestProperty("Connection", "Keep-Alive");//设置与服务器保持连接
            //connection.setRequestProperty("Accept-Language", "zh-CN,zh;0.9");

            // 连接
            conn.connect();

            DataOutputStream out = new DataOutputStream(conn.getOutputStream());
            out.writeBytes(new JSONObject(params).toString());
            //流用完记得关
            out.flush();
            out.close();

            if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                //获取响应
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String line;
                StringBuffer response = new StringBuffer();
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();

                JSONObject jsonObject = new JSONObject(response.toString());
                String respCode = jsonObject.optString("respCode");
                if ("000000".equals(respCode)) {
                    result = jsonObject.optString("data");
                } else {
                    apiErrorCallback(activity, apiCallback, respCode, jsonObject.optString("msg"));
                }
            } else {
                apiErrorCallback(activity, apiCallback, "-1", conn.getResponseCode() + " " + conn.getResponseMessage());
            }
            conn.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
            apiErrorCallback(activity, apiCallback, "-1", e.getMessage());
        }
        return result;
    }

    private static void apiErrorCallback(Activity activity, final DPlatformApiCallback apiCallback, final String code, final String msg) {
        if (null != apiCallback && null != activity) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Map<String, Object> data = new HashMap<>();
                    data.put("code", code);
                    data.put("msg", msg);
                    apiCallback.onResult(new JSONObject(data));
                }
            });
        }
    }

    static void go(Activity activity, DPlatformEvn evn, Map<String, Object> param, OnEncryptCallback encryptCallback, DPlatformApiCallback apiCallback) {
        new NetAsyncTask(activity, evn, encryptCallback, apiCallback).execute(param);
    }

    private final static class NetAsyncTask extends AsyncTask<Map<String, Object>, Void, Map<String, Object>> {
        private Dialog dialog;
        private OnEncryptCallback encryptCallback;
        private SharedPreferences sp;
        private DPlatformEvn evn;
        private DPlatformApiCallback apiCallback;
        private WeakReference<Activity> reference;

        NetAsyncTask(Activity activity, DPlatformEvn evn, OnEncryptCallback encryptCallback, DPlatformApiCallback apiCallback) {
            this.encryptCallback = encryptCallback;
            this.apiCallback = apiCallback;
            this.reference = new WeakReference<>(activity);
            this.evn = evn;
            crateDialog();
        }

        void crateDialog() {
            if (null != reference.get()) {
                this.sp = reference.get().getSharedPreferences("d_platform_uuid", MODE_PRIVATE);
                dialog = new Dialog(reference.get());
                dialog.setCanceledOnTouchOutside(false);
                dialog.setContentView(R.layout.net_post_loading);
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (null != dialog && !dialog.isShowing()) {
                dialog.show();
            }
        }

        @Override
        protected void onPostExecute(Map<String, Object> map) {
            super.onPostExecute(map);
            if (null != dialog && dialog.isShowing()) {
                dialog.dismiss();
            }
            if (null != map && null != encryptCallback) {
                encryptCallback.onEncrypt(map);
            }
        }

        @SafeVarargs
        @Override
        protected final Map<String, Object> doInBackground(Map<String, Object>... maps) {
            if (null != maps && maps.length > 0) {
                Map<String, Object> params = maps[0];
                try {
                    Object obj = params.get(Constant.Key.UUID);
                    if (null != obj) {
                        String uuid = String.valueOf(obj);
                        String result = sp.getString(uuid, null);
                        if (null != result && !"".equals(result.trim())) {
                            params.put(Constant.Key.TOKEN, result);
                            return params;
                        } else {
                            String url = URL_RELEASE;
                            if (DPlatformEvn.DEBUG.equals(evn)) {
                                url = URL_DEBUG;
                            } else if (DPlatformEvn.TEST.equals(evn)) {
                                url = URL_TEST;
                            } else if (DPlatformEvn.PRO.equals(evn)) {
                                url = URL_PPO;
                            }
                            String resp = post(reference.get(), url, params, apiCallback);
                            if (null != resp) {
                                sp.edit().putString(uuid, resp).apply();
                                params.put(Constant.Key.TOKEN, resp);
                                return params;
                            }
                        }
                    } else {
                        return params;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }

    public interface OnEncryptCallback {
        void onEncrypt(Map<String, Object> params);
    }
}
