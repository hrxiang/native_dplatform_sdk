function clicked(id, t, d){

  var orderSn  = "1664224458571280385";//后台返回的订单号
  var param = {
          action: 'pay',
          orderSn: orderSn,
          scheme:'game.dplatform.ls',
          token: '6ec0a768729f73159372e2a1df33b42a',
          channelNo:''
        };

  if(plus.runtime.isApplicationExist({pname:'com.dplatform.lite.ls',action:'org.dplatform.lite.ls://'})){
        if ( plus.os.name == "Android" ) {
          var urlScheme = "org.dplatform.lite.ls://dplatform.org?param="+JSON.stringify(param);
          plus.runtime.openURL( urlScheme, function (e){
            alert( "路径不对");
          } );
          } else if ( plus.os.name == "iOS" ) {
          var urlScheme = "org.dplatform.lite.ls://dplatform.org?param="+encodeURIComponent(JSON.stringify(param));
          plus.runtime.openURL( urlScheme, function (e){
          alert( "路径不对");
        } );
        }
  }else{
    if ( plus.os.name == "Android" ) {
      plus.runtime.openURL( "https://prod-up-app-read.obs.cn-north-4.myhuaweicloud.com/d-test/android/app-profile_ls.apk", function (e){

        } );
    } else if ( plus.os.name == "iOS" ) {
      plus.runtime.openURL( "itms-services://?action=download-manifest&url=https://prod-up-app-read.obs.cn-north-4.myhuaweicloud.com/d-test/ios/lisheng/d-ls-ios-manifest.plist", function (e){

      } );
    }
  }
}

   //联调下载地址 http://139.9.201.236/appdownload/ht;
   //测试下载地址 http://139.9.184.9/appdownload/ht
   //预发下载地址 https://dpl-pre.dpay.tech/appdownload/ht
   //生产下载地址 https://www.dpay.tech/appdownload/ht
    String PLATFORM_APP_DOWNLOAD_URL_TEST = "http://139.9.184.9/appdownload/ht";
    //预发
    String PLATFORM_APP_DOWNLOAD_URL_PRO = "https://dpl-pre.dpay.tech/appdownload/ht";
    //生产
    String PLATFORM_APP_DOWNLOAD_URL_RELEASE = "https://www.dpay.tech/appdownload/ht";