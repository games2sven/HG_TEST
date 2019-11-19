# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
  #greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
  -keep class org.greenrobot.greendao.**{*;}
  -dontwarn org.greenrobot.greendao.**
  -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
  public static java.lang.String TABLENAME;
  }

  -optimizationpasses 5
  -dontusemixedcaseclassnames
  -dontskipnonpubliclibraryclasses
  -dontpreverify
  -verbose
  -ignorewarnings
  -optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

  -keep public class * extends android.app.Activity
  -keep public class * extends android.app.Application
  -keep public class * extends android.app.Service
  -keep public class * extends android.content.BroadcastReceiver
  -keep public class * extends android.content.ContentProvider
  -keep public class * extends android.app.backup.BackupAgentHelper
  -keep public class * extends android.preference.Preference
  -keep public class com.android.vending.licensing.ILicensingService
  -keep public class * extends android.os.AsyncTask
  #加入第三方supportv4包的混淆过滤
  -dontwarn android.support.v4.**
  -dontwarn android.support.v7.**
  -keep class android.support.v7.**
  -keep class android.support.v4.** { *;}
  -keep interface android.support.v4.** { *; }
  -keep interface android.support.v7.** { *; }
  -keep public class * extends android.support.v4.**
  -keep public class * extends android.support.v7.**
  -keep public class * extends android.app.Fragment

  -keep class com.zerotech.FFmpegMediaMetadataRetriever { *; }
  -keep class com.highgreat.education.fragment.** { *;}
  -keep class com.highgreat.education.activity.** { *;}

  -keep class com.zero.sdk.ffmpeg.**{ *; }
  -keep interface com.highgreat.education.greendao.** { *; }
  -keep class com.highgreat.education.bean.** { *;}
  -keep class de.hdodenhof.circleimageview.** { *;}

  -keep class android.support.design.widget.TabLayout{*;}
  -keep class android.support.design.internal.**{*;}
  -keep class android.support.design.widget.**{*;}
  #友盟统计混淆问题
  #-dontwarn com.umeng.**
  #-keep class com.umeng*.** {*; }

  #增加对微信混淆的支持，以避免混淆以后无法弹出发送第三方消息的确认框
  -keep class com.tencent.mm.sdk.openapi.WXMediaMessage { *;}
  -keep class com.tencent.mm.sdk.openapi.** implements com.tencent.mm.sdk.openapi.WXMediaMessage$IMediaObject {*;}
  -dontwarn com.tencent.mm.**
  -keep class com.tencent.mm.**{ *;}
  -keep class com.baidu.speech.**{*;}
  -keep class com.huawei.android.**{*;}
  -keepattributes *Annotation*
  -keepattributes *JavascriptInterface*

  -keepclasseswithmembernames class * {
      native <methods>;
  }

  -keepclasseswithmembers class * {
      public <init>(android.content.Context);
  }

  -keepclasseswithmembers class * {
      public <init>(android.content.Context, android.util.AttributeSet);
  }

  -keepclasseswithmembers class * {
      public <init>(android.content.Context, android.util.AttributeSet, int);
  }

  -keepclassmembers class * extends android.app.Activity {
      public void *(android.view.View);
  }

  -keepclasseswithmembernames class *{
      native <methods>;
  }

  -keepclassmembers enum * {
      public static **[] values();
      public static ** valueOf(java.lang.String);
  }

  -keep class * implements android.os.Parcelable {
      public static final android.os.Parcelable$Creator *;
      static ** CREATOR;
      static ** SavedState;
  }

  #libs下的
  #-libraryjars libs/bolts-android-1.1.4.jar
  -keep class bolts.**{*;}
  -dontwarn  bolts.**

  #-libraryjars libs/commons-codec-1.3.jar
  -keep class org.apache.commons.codec.**{*;}
  -dontwarn  org.apache.commons.codec.**

  #-libraryjars libs/commons-net-3.2.jar
  -keep class org.apache.commons.net.**{*;}
  -dontwarn  org.apache.commons.net.**

  #-libraryjars libs/gson-2.5.jar
  -keep class com.google.gson.**{*;}
  -dontwarn  com.google.gson.**

  #-libraryjars libs/httpmime-4.1.3.jar
  -keep class org.apache.http.entity.mime.**{*;}
  -dontwarn  org.apache.http.entity.mime.**
  -keep class com.hisilicon.camplayer.**{*;}
  -keep class com.hisilicon.dv.**{*;}
  -keep class com.sevenheaven.segmentcontrol.**{*;}

  -keep class com.iflytek.**{*;}
  -keep class cn.chinaMobile.**{*;}

  #第三方de
  -keep class cn.bingoogolapple.badgeview.**{*;}
  -keep class com.afollestad.materialdialogs.**{*;}
  -keep class com.liulishuo.filedownloader.**{*;}
  -keep class me.zhanghai.android.materialprogressbar.**{*;}
  -keep class com.nineoldandroids.**{*;}
  -keep class com.pnikosis.materialishprogress.**{*;}
  -keep class com.squareup.okhttp.**{*;}

  #okhttputils
  -dontwarn com.zhy.http.**
  -keep class com.zhy.http.**{*;}
  #okhttp
  -dontwarn okhttp3.**
  -keep class okhttp3.**{*;}
  #okio
  -dontwarn okio.**
  -keep class okio.**{*;}

  -keep class z.sye.space.**{*;}
  -keep class uk.co.senab.photoview.**{*;}
  -keep class com.squareup.picasso.**{*;}
  -keep class android.support.v7.**{*;}
  -keep class com.makeramen.roundedimageview.**{*;}
  -keep class com.tonicartos.superslim.**{*;}
  -keep class android.support.annotation.**{*;}
  -keep class com.microsoft.projectoxford.emotion.**{*;}
  -keep class com.microsoft.projectoxford.face.**{*;}
  #-keep class com.highgreat.hgfly.dobby.microsoftface.helper.FaceMark{*;}

  #如果用用到Gson解析包的，直接添加下面这几行就能成功混淆，不然会报错。
  #gson
  #-libraryjars libs/gson-2.2.2.jar
  -keepattributes Signature
  # Gson specific classes
  -keep class sun.misc.Unsafe { *; }
  # Application classes that will be serialized/deserialized over Gson
  -keep class com.google.gson.examples.android.model.** { *; }

  #butternife的混淆
  -keep class butterknife.** { *; }
  -dontwarn butterknife.internal.**
  -keep class **$$ViewBinder { *; }
  -keepclasseswithmembernames class * {
      @butterknife.* <fields>;
  }
  -keepclasseswithmembernames class * {
      @butterknife.* <methods>;
  }

  -dontwarn org.apache.**
   #忽略警告
  -dontwarn com.iflytek.common.**
   #保留一个完整的包
  -keep class com.iflytek.common.** { *; }
  -dontwarn com.squareup.**
  -dontwarn okio.**

  -keep class com.iflytek.common.** { *; }

  -keep class org.greenrobot.eventbus.** { *; }
  -keepclassmembers class ** {
          public void onEvent*(**);
          public void onEventMainThread*(**);
      }

      # Only required if you use AsyncExecutor
  -keepclassmembers class * extends de.greenrobot.event.util.ThrowableFailureEvent {
          <init>(java.lang.Throwable);
      }

  #分享
  -keep class com.alipay.share.sdk.**{*;}
  -keep class com.tencent.**{*;}
  -keep class com.facebook.**{*;}
  -keep class twitter4j.**{*;}
  -keep class com.sina.**{*;}
  -keep class javax.**
  -keep class org.apache.**
  #picasso
  -dontwarn com.squareup.okhttp.**
  #gson
  -keep class com.google.**
  -keep class com.nuance.**{*;}
  #语音识别

  -keep class com.alibaba.sdk.android.**{*;}
  -keep class com.bumptech.glide.integration.**{*;}

  -keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
      **[] $VALUES;
      public *;
  }

  -dontwarn com.tencent.bugly.**
  -keep public class com.tencent.bugly.**{*;}

  -keep class cn.sharesdk.**{*;}
  -keep class com.mob.**{*;}
  -dontwarn com.mob.**
  -dontwarn cn.sharesdk.**

  #友盟统计
  -keepclassmembers class * {
      public <init> (org.json.JSONObject);
  }

  -keep public class com.highgreat.education.R$*{
      public static final int *;
  }

  -keepclassmembers enum * {
      public static **[] values();
      public static ** valueOf(java.lang.String);
  }

  #极光推送
  -dontoptimize
  -dontwarn cn.jpush.**
  -keep class cn.jpush.**{ *;}
  #==================gson && protobuf==========================
  -dontwarn com.google.**
  -keep class com.google.gson.** {*;}
  -keep class com.google.protobuf.** {*;}



  -keep class com.rtspclient.** {*;}
  -keep class com.dash.** {*;}
  -keep class com.logcat.** {*;}
  -keep class com.rt_ufo_together.** {*;}
  -keep class com.runtop.** {*;}
  -keep class org.libsdl.app.** {*;}




  #ijkplayer
  -keep class tv.danmaku.ijk.media.player.** {*; }
  -keep class tv.danmaku.ijk.media.player.IjkMediaPlayer{*;}
  -keep class tv.danmaku.ijk.media.player.ffmpeg.FFmpegApi{*;}

  # MAVLink相关混淆
  -keep class com.MAVLink.**{*;}
  -keep class com.MAVLink.common.**{*;}
  -keep class com.MAVLink.enums.**{*;}
  -keep class com.MAVLink.Messages.**{*;}



 #greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
 -keep class org.greenrobot.greendao.**{*;}
 -keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
 public static java.lang.String TABLENAME;
 }
 -keep class **$Properties




 -dontwarn javax.annotation.**
 -dontwarn javax.inject.**
 # OkHttp3
 -dontwarn okhttp3.logging.**
 -keep class okhttp3.internal.**{*;}
 -dontwarn okio.**
 # Retrofit
 -dontwarn retrofit2.**
 -keep class retrofit2.** { *; }
 #-keepattributes Signature-keepattributes Exceptions
 # RxJava RxAndroid
 -dontwarn sun.misc.**
 -keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
     long producerIndex;
     long consumerIndex;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode producerNode;
 }
 -keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
     rx.internal.util.atomic.LinkedQueueNode consumerNode;
 }

 # Gson
 -keep class com.google.gson.stream.** { *; }
 -keepattributes EnclosingMethod
 -keep class com.highgreat.education.bean.**{*;}
 -keep class com.hoho.android.usbserial.**{*;}

 -keep class com.wang.avi.** { *; }
 -keep class com.wang.avi.indicators.** { *; }