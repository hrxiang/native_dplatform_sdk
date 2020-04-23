# native_dplatform_sdk

## 1,

               //创建api，并设置站点
               api = DPlatformApiFactory.createApi(this, "ls", null, null);

               注： 第一个参数上下文，必传
                   第二个参数站点，必传
                   第三个参数你的scheme
                   第二个参数平台环境


## 2,
               //响应结果监听
               api.setCallback(new DPlatformApiCallback() {
                   @Override
                   public void onResult(JSONObject object) {

                   }
               });


## 3,
               //传递参数
               api.putParameter("action", "auth");
               api.putParameter("token", "9527");
               api.putParameter("isMock", 1);

## 4,
               //发送请求
                api.sendReq();