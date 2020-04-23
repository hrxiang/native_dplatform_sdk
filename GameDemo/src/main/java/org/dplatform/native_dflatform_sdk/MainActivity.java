package org.dplatform.native_dflatform_sdk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.dplatform.sdk.DPlatformApi;
import org.dplatform.sdk.DPlatformApiCallback;
import org.dplatform.sdk.DPlatformApiFactory;
import org.dplatform.sdk.PayModel;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    DPlatformApi api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView text = findViewById(R.id.text);

        //创建api，并设置站点 org.dplatform.game.cs
        api = DPlatformApiFactory.createApi(this, "cs", null);


        //传递参数
        api.putParameter("action", "auth");
        api.putParameter("token", "9527");
        api.putParameter("isMock", 1);

        //响应结果监听
        api.setCallback(new DPlatformApiCallback() {
            @Override
            public void onResult(JSONObject object) {
                text.setText(object.toString());
            }
        });

        //提交订单10001
        findViewById(R.id.bt_auth).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //发送请求
                api.sendReq();
            }
        });

    }

    /**
     * 提交订单1
     **/
    private void submitOrder1() {
        //传递参数
        api.putParameter("action", "pay");
        api.putParameter("orderSn", "no1");
        api.putParameter("token", "9527");
        //发送请求
        api.sendReq();
    }

    /**
     * 提交订单2
     **/
    private void submitOrder2() {
        //传递参数
        PayModel model = new PayModel();
        model.setOrderSn("no2");
        model.setToken("9527");
        model.setScheme("org.dplatform.game");
        api.putModel(model);
        //发送请求
        api.sendReq();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.onNewIntent(intent);
    }
}