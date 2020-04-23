package com.example.application;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    String callbackScheme = null;
    String defaultScheme = null;
    String packageName = null;
    JSONObject object = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView text = findViewById(R.id.text);
        if (null != getIntent()) {
            Uri uri = getIntent().getData();
            if (null != uri) {
                callbackScheme = uri.getQueryParameter("callbackScheme");
                packageName = uri.getQueryParameter("packageName");
                try {
                    object = new JSONObject(uri.getQueryParameter("params"));
                    defaultScheme = object.optString("scheme");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                text.setText("收到：" + Uri.decode(uri.toString()));
            }
        }

        findViewById(R.id.btn_login1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != defaultScheme) {
                    openUri(MainActivity.this, defaultScheme + "://do?action=login&code=0&msg=通过首页scheme传递数据");
                }
                finish();
            }
        });
        findViewById(R.id.btn_login2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (null != callbackScheme) {
                    openUri(MainActivity.this, callbackScheme + "://do?action=login&code=0&msg=通过透明页scheme传递数据");
                }
                finish();
            }
        });
        findViewById(R.id.btn_login3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                send(MainActivity.this);
                finish();
            }
        });
    }

    public void openUri(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public void send(Context context) {
        Intent i = new Intent(packageName + ".action.dplatform_sdk_callback_000000");
        Map<String, String> par = new HashMap<>();
        par.put("action", "login");
        par.put("desc", "通过广播回传数据");
        i.putExtra("dplatformResp", new JSONObject(par).toString());
        context.sendBroadcast(i);
    }
}
