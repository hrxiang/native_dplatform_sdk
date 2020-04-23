


[![](https://jitpack.io/v/hrxiang/native_dplatform_sdk.svg)](https://jitpack.io/#hrxiang/native_dplatform_sdk)

implementation 'com.github.hrxiang:native_dplatform_sdk:Tag'

## step 1

           <activity
               android:name="org.dplatform.native_dflatform_sdk.MainActivity"
               android:launchMode="singleTop">
               <intent-filter>
                   <action android:name="android.intent.action.MAIN" />

                   <category android:name="android.intent.category.LAUNCHER" />
               </intent-filter>
               <intent-filter>
                   <data android:scheme="你的scheme" />

                   <action android:name="android.intent.action.VIEW" />

                   <category android:name="android.intent.category.DEFAULT" />
               </intent-filter>
           </activity>

## step 2

           //创建api，并设置站点
           api = DPlatformApiFactory.createApi(this, "ls", null, null);

           第1个参数：上下文（必传）
           第2个参数：站点（必传）
           第3个参数：你的scheme
           第4个参数：平台环境（DPlatformEvn.class）


## step 3

           //响应结果监听
           api.setCallback(new DPlatformApiCallback() {
               @Override
               public void onResult(JSONObject object) {

              }
           });

## step 4

          @Override
          protected void onNewIntent(Intent intent) {
              super.onNewIntent(intent);
              api.onNewIntent(intent);
          }


## step 5

          //传递参数
          api.putParameter("action", "auth");
          api.putParameter("token", "9527");
          api.putParameter("isMock", 1);



## step 6

          //发送请求
          api.sendReq();
