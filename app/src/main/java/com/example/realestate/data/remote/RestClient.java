package com.example.realestate.data.remote;

import android.content.Context;
import android.util.Log;

import com.example.realestate.BuildConfig;
import com.example.realestate.utils.AndroidUtilities;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.http.conn.ssl.StrictHostnameVerifier;

import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author anhquoc09
 * @since 06/03/2019
 */
public class RestClient {

    public static final String TAG = RestClient.class.getSimpleName();

    private static final String baseUrl = "https://www.google.com.vn/";

    private static final long CACHE_SIZE = 10 * 1024 * 1024;

    private Retrofit mRetrofit;

    private static volatile RestClient sInstance = null;

    private final OkHttpClient mClient;

    private final Gson mGson;

    private RxJavaCallAdapterFactory mRxJavaCallAdapterFactory;

    private RestClient(Context context) {
        final Cache cache = new Cache(context.getCacheDir(), CACHE_SIZE);

        final Interceptor requestInterceptor = new DefaultParamsInterceptor(
                AndroidUtilities.getDeviceId(),
                AndroidUtilities.getDeviceName(),
                BuildConfig.VERSION_NAME);

        final HttpLoggingInterceptor httpLoggingInterceptor = defaultLoggingInterceptor();

        mClient = createClient(cache, null, requestInterceptor,
                httpLoggingInterceptor, 60, 60, 60);

        mGson = new GsonBuilder().create();

        mRxJavaCallAdapterFactory = RxJavaCallAdapterFactory.create();

        mRetrofit = createRetrofit(baseUrl, mClient, mGson, mRxJavaCallAdapterFactory);
    }

    public static RestClient getInstance(Context context) {
        RestClient localInstance = sInstance;
        if (localInstance == null) {
            synchronized (RestClient.class) {
                localInstance = sInstance;
                if (localInstance == null) {
                    localInstance = sInstance = new RestClient(context);
                }
            }
        }
        return localInstance;
    }

    public <T> T create(Class<T> service) {
        return mRetrofit.create(service);
    }

    private HttpLoggingInterceptor defaultLoggingInterceptor() {
        HttpLoggingInterceptor.Logger logger = message -> {
            if (BuildConfig.DEBUG) {
                Log.i(TAG, message);
            }
        };
        HttpLoggingInterceptor.Level level = BuildConfig.DEBUG ?
                HttpLoggingInterceptor.Level.BODY :
                HttpLoggingInterceptor.Level.NONE;

        return createLoggingInterceptor(logger, level);
    }

    private HttpLoggingInterceptor createLoggingInterceptor(HttpLoggingInterceptor.Logger logger,
                                                            HttpLoggingInterceptor.Level level) {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(logger);
        httpLoggingInterceptor.setLevel(level);
        return httpLoggingInterceptor;
    }

    private OkHttpClient createClient(Cache cache,
                                      SSLContext sslContext,
                                      Interceptor requestInterceptor,
                                      HttpLoggingInterceptor loggingInterceptor,
                                      long readTimeOut /* seconds */,
                                      long writeTimeout /* seconds */,
                                      long connectionTimeout /* seconds */) {
        final OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (cache != null) {
            builder.cache(cache);
        }
        if (requestInterceptor != null) {
            builder.addInterceptor(requestInterceptor);
        }
        if (loggingInterceptor != null) {
            builder.addInterceptor(loggingInterceptor);
        }
        if (sslContext != null) {
            builder.sslSocketFactory(sslContext.getSocketFactory());
            builder.hostnameVerifier(new StrictHostnameVerifier());
        }
        builder.readTimeout(readTimeOut, TimeUnit.SECONDS);
        builder.writeTimeout(writeTimeout, TimeUnit.SECONDS);
        builder.connectTimeout(connectionTimeout, TimeUnit.SECONDS);
        return builder.build();
    }

    private Retrofit createRetrofit(String baseUrl,
                                    OkHttpClient client,
                                    Gson gson,
                                    CallAdapter.Factory callAdapterFactory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(callAdapterFactory)
                .build();
    }
}
