package org.dplatform.native_dflatform_sdk;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.dplatform.sdk.DPlatformApi;
import org.dplatform.sdk.DPlatformApiCallback;
import org.dplatform.sdk.DPlatformApiFactory;
import org.dplatform.sdk.DPlatformEvn;
import org.dplatform.sdk.OnCheckReqUriListener;
import org.dplatform.sdk.PayModel;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements DPlatformApiCallback, RadioGroup.OnCheckedChangeListener, View.OnClickListener, CompoundButton.OnCheckedChangeListener, InputFilter {
    DPlatformApi api;
    EditText token;
    EditText orderSn;
    EditText site;
    TextView result;
    LinearLayout otherParams;

    boolean isLogin = true;
    boolean isMock = true;
    String siteStr = "wb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        //创建api，并设置站点 org.dplatform.game.cs
        api = DPlatformApiFactory.createApi(this, siteStr, DPlatformEvn.TEST);

        site.setText(siteStr);

        //响应结果监听
        api.setCallback(this);

        api.setCheckReqUriListener(new OnCheckReqUriListener() {
            @Override
            public boolean isValidReqUri(Uri uri) {
                return true;
            }
        });
    }

    /**
     * 提交订单1
     **/
    private void submitOrder1() {
        //传递参数
        api.putParameter("action", "pay");
        api.putParameter("token", "9527");
        api.putParameter("orderSn", "no10086");
        api.putParameter("isMock", "1");
        //发送请求
        api.sendReq();
    }

    /**
     * 提交订单2
     **/
    private void submitOrder2() {
        //传递参数
        PayModel model = new PayModel();
        model.setToken("9527");
        model.setOrderSn("no10086");
        api.putModel(model);
        //发送请求
        api.sendReq();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        api.onNewIntent(intent);
    }


    @Override
    public void onClick(View view) {
        if (site.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入站点", Toast.LENGTH_SHORT).show();
            return;
        }
        if (token.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入token", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!isLogin && orderSn.getText().toString().isEmpty()) {
            Toast.makeText(this, "请输入orderSn", Toast.LENGTH_SHORT).show();
            return;
        }

        api.site(site.getText().toString());
        api.getParameters().clear();
        api.putParameter("isMock", isMock ? "1" : "0");
        api.putParameter("action", isLogin ? "auth" : "pay");
        api.putParameter("token", token.getText().toString());
        if (!TextUtils.isEmpty(orderSn.getText().toString())) {
            api.putParameter("orderSn", orderSn.getText().toString());
        }
        putOtherParams();
        api.sendReq();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        isLogin = isLogin(i);
        orderSn.setVisibility(isLogin ? View.GONE : View.VISIBLE);
    }

    static boolean isLogin(int id) {
        return R.id.radio_login == id;
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        isMock = b;
    }

    @Override
    public void onResult(JSONObject object) {
        result.setText("返回结果：" + object.toString());
    }

    @Override
    public CharSequence filter(CharSequence charSequence, int i, int i1, Spanned spanned, int i2, int i3) {
        if (charSequence.equals(" ")) return "";
        else return null;
    }

    ViewGroup addParamView() {
        final ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(this).inflate(R.layout.item_params, null);
        otherParams.addView(viewGroup);
        viewGroup.getChildAt(3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                otherParams.removeView(viewGroup);
            }
        });
        return viewGroup;
    }

    void putOtherParams() {
        int count = otherParams.getChildCount();
        String key, value;
        ViewGroup vg;
        View inputKey;
        View inputValue;
        for (int i = 0; i < count; i++) {
            if (otherParams.getChildAt(i) instanceof ViewGroup) {
                vg = (ViewGroup) otherParams.getChildAt(i);
                if (vg.getChildCount() == 4) {
                    inputKey = vg.getChildAt(0);
                    inputValue = vg.getChildAt(2);
                    if (inputKey instanceof EditText && inputValue instanceof EditText) {
                        key = ((EditText) inputKey).getText().toString().trim();
                        value = ((EditText) inputValue).getText().toString().trim();
                        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(value)) {
                            System.out.println("=====key:" + key + "=======value:" + value);
                            api.putParameter(key, value);
                        }
                    }
                }
            }
        }
    }

    void initView() {
        result = findViewById(R.id.result);
        token = findViewById(R.id.token);
        site = findViewById(R.id.site);
        orderSn = findViewById(R.id.orderSn);
        ((RadioGroup) findViewById(R.id.selectAction)).setOnCheckedChangeListener(this);
        ((CheckBox) findViewById(R.id.icMock)).setOnCheckedChangeListener(this);
        findViewById(R.id.submit).setOnClickListener(this);
        token.setFilters(new InputFilter[]{this});
        orderSn.setFilters(new InputFilter[]{this});
        site.setFilters(new InputFilter[]{this});
        otherParams = findViewById(R.id.otherParams);
        findViewById(R.id.addParams).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addParamView();
            }
        });
    }
}