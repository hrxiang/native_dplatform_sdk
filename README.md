


[![](https://jitpack.io/v/hrxiang/native_dplatform_sdk.svg)](https://jitpack.io/#hrxiang/native_dplatform_sdk)

## 引入SDK


          allprojects {
              repositories {
                    ...
                  maven { url 'https://jitpack.io' }
              }
          }

          dependencies {
          	  implementation 'com.github.hrxiang:native_dplatform_sdk:Tag'
          }


## 你的应用的scheme格式

        org.dplatform.game.你的站点.你的应用包名

## step 1：添加你的scheme到manifest

           <activity
               android:name="org.dplatform.native_dflatform_sdk.MainActivity"
               android:launchMode="singleTop">
               <intent-filter>
                   <action android:name="android.intent.action.MAIN" />

                   <category android:name="android.intent.category.LAUNCHER" />
               </intent-filter>
               <intent-filter>
                   <data android:scheme="org.dplatform.game.你的站点.${applicationId}" />

                   <action android:name="android.intent.action.VIEW" />

                   <category android:name="android.intent.category.DEFAULT" />
               </intent-filter>
           </activity>

## step 2：创建api，默认环境为生产环境

           //创建api，并设置站点
           api = DPlatformApiFactory.createApi(this, "你的站点", null);

           第1个参数：上下文（必传）
           第2个参数：站点（必传）
           第3个参数：平台环境（DPlatformEvn.class），默认为生产环境


## step 3：获取平台返回的数据

           //响应结果监听
           api.setCallback(new DPlatformApiCallback() {
               @Override
               public void onResult(JSONObject object) {

              }
           });

## step 4：覆盖activity的onNewIntent方法

          @Override
          protected void onNewIntent(Intent intent) {
              super.onNewIntent(intent);
              api.onNewIntent(intent);
          }


## step 5：添加数据，以下两种方式等价

          //通用方式传递数据，action值必须传递，如支付pay，登录：auth
          api.putParameter("action", "pay");
          api.putParameter("orderSn", "no1110");
          api.putParameter("token", "9527");

          或 SDK已定义好的数据格式

          //如：充值
          PayModel model = new PayModel();
          model.setOrderSn("no1110");//订单号
          model.setToken("9527");//登录token
          api.putModel(model);



## step 6：发送请求

          //发送请求
          api.sendReq();


## 扩展：

        如果你想使用其他scheme：
        第一步： 替换manifest里的scheme
        第二步： api.putParameter("scheme", "xxxxxxx");//传递你的scheme

