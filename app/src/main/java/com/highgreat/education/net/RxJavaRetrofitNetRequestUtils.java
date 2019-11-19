package com.highgreat.education.net;

import android.support.annotation.NonNull;
import android.util.Log;

import com.highgreat.education.MyApp;
import com.highgreat.education.bean.APPBean;
import com.highgreat.education.bean.BaseHttpBean;
import com.highgreat.education.bean.EventCenter;
import com.highgreat.education.bean.UpgradePackageBean;
import com.highgreat.education.common.ServerLinks;
import com.highgreat.education.utils.LogUtil;
import com.highgreat.education.utils.Wifi4GUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class RxJavaRetrofitNetRequestUtils implements  BaseRequest {

    private static final long DEFAULT_TIMEOUT = 5;
    PersonCenterApi mApi;
    public RxJavaRetrofitNetRequestUtils() {
        Wifi4GUtils.bringUpWifiNetwork(MyApp.getAppContext());
        OkHttpClient okHttpClient  =new OkHttpClient.Builder().connectTimeout(DEFAULT_TIMEOUT,TimeUnit.SECONDS).readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS).
                writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                okhttp3.Response  response = chain.proceed(chain.request());

                MediaType mediaType = response.body().contentType();
                byte[] responseBytes = response.body().bytes();

                String  content =new String(responseBytes);

                checkCode(content);
                response = response.newBuilder()
                        .body(ResponseBody.create(mediaType, responseBytes))
                        .build();
              return  response;
            }
        })

                .retryOnConnectionFailure(false)

                        .build();
        Retrofit.Builder  builder  =new Retrofit.Builder().baseUrl(ServerLinks.SERVER_IP)//基础URL 建议以 / 结尾
                .addConverterFactory(GsonConverterFactory.create())//设置 Json 转换器
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient);//RxJava 适配器
            mApi = builder.build().create(PersonCenterApi.class);

    }

    private void checkCode(String content) {
        try {
            Log.i("Sven","content = "+content);
            JSONObject  jsonObject = new JSONObject(content);
            int status = jsonObject.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public RxJavaRetrofitNetRequestUtils(JsDownloadListener listenert) {
        JsDownloadInterceptor mInterceptor = new JsDownloadInterceptor(listenert);
        OkHttpClient   mClient =new OkHttpClient.Builder()
                .addInterceptor(mInterceptor)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build();
        Retrofit.Builder  builder  =new Retrofit.Builder().baseUrl(ServerLinks.SERVER_IP)//基础URL 建议以 / 结尾
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).client(mClient);//RxJava 适配器
        mApi = builder.build().create(PersonCenterApi.class);
    }



    @Override
    public void verifySignCode(Map<String,String> params , Subscriber<BaseHttpBean> subscriber) {
        mSubscriber.add(subscriber);
        mApi.verifySignCode(params ,RestClientWithSSID.getHeaders()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }
    @Override
    public void verifyPassCode(String phone,String passcode, Subscriber<BaseHttpBean> subscriber) {
        mSubscriber.add(subscriber);
        mApi.verifyPassCode(phone,passcode,RestClientWithSSID.getHeaders()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void getSignCode(String phone, Subscriber<Response<BaseHttpBean>> subscriber) {
        mSubscriber.add(subscriber);
        mApi.getVerifySignCode(phone,RestClientWithSSID.getHeaders()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void checkIsSign(String phone, Subscriber<Response<BaseHttpBean>> subscriber) {
        mSubscriber.add(subscriber);
        mApi.checkIsSign(phone).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void checkIsCanRetrieve(String phone, Subscriber<Response<BaseHttpBean>> subscriber) {
        mSubscriber.add(subscriber);
        mApi.checkIsCanRetrieve(phone,RestClientWithSSID.getHeaders()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void reSetPassword(int type,String password, Subscriber<BaseHttpBean> subscriber) {
        mSubscriber.add(subscriber);
        if (type==1){
            mApi.signSetPwd(password,RestClientWithSSID.getHeaders()).subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        }else {
            mApi.reSetPwd(password,RestClientWithSSID.getHeaders()).subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
        }

    }

    @Override
    public void getAppUpdateInfo(Subscriber<APPBean> subscriber) {
        mSubscriber.add(subscriber);
        mApi.getAppUpdateInfo(RestClientWithSSID.getHeaders()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    @Override
    public void getFirmwareInfo(Map<String, String> params, Subscriber<UpgradePackageBean> subscriber) {
        mSubscriber.add(subscriber);
        mApi.getDroneFirnWare(params,RestClientWithSSID.getHeaders()).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    List<Subscriber> mSubscriber = new ArrayList<>();

    @Override
    public  void  cancel(){
        if (mSubscriber!=null) {
            for (Subscriber subscriber : mSubscriber) {
                subscriber.unsubscribe();
            }
            mSubscriber.clear();
        }

    }

    @Override
    public void cancelDownload() {
        if (subscriber!=null){
            subscriber.unsubscribe();
        }
    }


    Subscriber  subscriber;

    @Override
    public void download(@NonNull String url, final String filePath, Subscriber subscriber) {
        this.subscriber=subscriber;
        mApi.downFile(url).subscribeOn(Schedulers.io()).map(new Func1<ResponseBody, InputStream>() {
            @Override
            public InputStream call(ResponseBody responseBody) {
                return  responseBody.byteStream();
            }
        }).observeOn(Schedulers.computation()).doOnNext(new Action1<InputStream>() {
            @Override
            public void call(InputStream inputStream) {
                writeFile(inputStream,filePath);
            }
        }).subscribeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);

    }




    @Override
    public void sendRepair(Map<String, String> params, Subscriber<BaseHttpBean> subscriber, MultipartBody.Part file) {
        mSubscriber.add(subscriber);
            mApi.sendFauiltRepair(params,RestClientWithSSID.getHeaders(),file).subscribeOn(Schedulers.io()).
                    observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }


    /**
     * Description: 下载进度回调
     */
    public interface JsDownloadListener {

        void onStartDownload();

        void onProgress(int progress);

        void onFinishDownload();

        void onFail(String errorInfo);

    }


    /**
     * Description: 带进度 下载请求体
     */
    public class JsResponseBody extends ResponseBody {

        private ResponseBody responseBody;

        private JsDownloadListener downloadListener;

        // BufferedSource 是okio库中的输入流，这里就当作inputStream来使用。
        private BufferedSource bufferedSource;

        public JsResponseBody(ResponseBody responseBody, JsDownloadListener downloadListener) {
            this.responseBody = responseBody;
            this.downloadListener = downloadListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    LogUtil.e("download", "read: "+ (int) (totalBytesRead * 100 / responseBody.contentLength()));
                    if (null != downloadListener) {
                        if (bytesRead != -1) {
                            downloadListener.onProgress((int) (totalBytesRead * 100 / responseBody.contentLength()));
                        }

                    }
                    return bytesRead;
                }
            };

        }
    }

    /**
     * Description: 带进度 下载  拦截器
     */
    public class JsDownloadInterceptor implements Interceptor {

        private JsDownloadListener downloadListener;

        public JsDownloadInterceptor(JsDownloadListener downloadListener) {
            this.downloadListener = downloadListener;
        }

        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response  response = chain.proceed(chain.request());
            return response.newBuilder().body(
                    new JsResponseBody(response.body(), downloadListener)).build();
        }
    }

    /**
     * 将输入流写入文件
     * @param inputString
     * @param filePath
     */
    private void writeFile(InputStream inputString, String filePath) {

        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);

            byte[] b = new byte[1024];

            int len;
            while ((len = inputString.read(b)) != -1) {
                fos.write(b,0,len);
            }
            inputString.close();
            fos.close();

        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        }

    }


}
