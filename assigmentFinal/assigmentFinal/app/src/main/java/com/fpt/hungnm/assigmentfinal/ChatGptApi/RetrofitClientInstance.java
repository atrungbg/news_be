package com.fpt.hungnm.assigmentfinal.ChatGptApi;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {
    private static Retrofit retrofit;
    private static final String BASE_URL = "https://api.openai.com/v1/";

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(getOkHttpClient())
                    .build();
        }
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        return new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer sk-JyYYfgfCtAKbjG3W_P4eBp2H7TBblwScb0lLyk2owuT3BlbkFJTD04aUgNxxCkOVK65jP1c9i0i4vT_NFDgBfD5hho0A") // Thay YOUR_API_KEY_HERE bằng API key của bạn
                            .build();
                    return chain.proceed(request);
                })
                .build();
    }
}

