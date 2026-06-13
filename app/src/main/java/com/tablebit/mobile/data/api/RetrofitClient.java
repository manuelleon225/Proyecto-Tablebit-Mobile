package com.tablebit.mobile.data.api;

import com.tablebit.mobile.data.mock.MockApiService;
import com.tablebit.mobile.data.mock.MockData;
import com.tablebit.mobile.session.SessionManager;
import com.tablebit.mobile.session.TokenManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static RetrofitClient instance;
    private final Retrofit retrofit;
    private final ApiService apiService;

    private static final boolean IS_DEBUG = false;
    private static boolean useMockMode = false;
    private static MockApiService mockApiService;

    private RetrofitClient(TokenManager tokenManager) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        if (IS_DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);
        } else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(tokenManager))
                .addInterceptor(logging)
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        retrofit = new Retrofit.Builder()
                .baseUrl(NetworkConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        apiService = retrofit.create(ApiService.class);
    }

    public static void setMockMode(boolean mock) {
        useMockMode = mock;
        if (mock && mockApiService == null) {
            mockApiService = new MockApiService();
        }
    }

    public static boolean isMockMode() {
        return useMockMode;
    }

    public static synchronized RetrofitClient getInstance(TokenManager tokenManager) {
        if (instance == null) {
            instance = new RetrofitClient(tokenManager);
        }
        return instance;
    }

    public static synchronized RetrofitClient getInstance(SessionManager sessionManager) {
        return getInstance(sessionManager.getTokenManager());
    }

    public static void resetInstance() {
        instance = null;
    }

    public ApiService getApiService() {
        if (useMockMode && mockApiService != null) {
            return mockApiService;
        }
        return apiService;
    }
}
