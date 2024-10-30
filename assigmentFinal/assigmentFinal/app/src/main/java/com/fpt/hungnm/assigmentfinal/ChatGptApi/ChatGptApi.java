package com.fpt.hungnm.assigmentfinal.ChatGptApi;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ChatGptApi {
    @POST("chat/completions")
    Call<ChatGptResponse> getAllTransactionsByString(@Body ChatGptRequest request);
}
