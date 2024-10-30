//package com.fpt.hungnm.assigmentfinal;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.fpt.hungnm.assigmentfinal.ChatGptApi.ChatGptApi;
//import com.fpt.hungnm.assigmentfinal.ChatGptApi.ChatGptRequest;
//import com.fpt.hungnm.assigmentfinal.ChatGptApi.ChatGptResponse;
//import com.fpt.hungnm.assigmentfinal.ChatGptApi.Message;
//import com.fpt.hungnm.assigmentfinal.ChatGptApi.RetrofitClientInstance;
//import com.google.gson.Gson;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;
//
//public class ChatActivity extends AppCompatActivity {
//
//    private EditText messageInput;
//    private LinearLayout messageContainer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_chat);
//
//        messageInput = findViewById(R.id.messageInput);
//        messageContainer = findViewById(R.id.messageContainer);
//        Button sendButton = findViewById(R.id.sendButton);
//
//        sendButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendMessage();
//            }
//        });
//    }
//
//    private void sendMessage() {
//        String userMessage = messageInput.getText().toString();
//        if (!userMessage.isEmpty()) {
//            // Hiển thị tin nhắn trong UI
//            TextView messageView = new TextView(this);
//            messageView.setText("Bạn: " + userMessage);
//            messageContainer.addView(messageView);
//
//            // Tạo danh sách các thông điệp
//            List<Message> messages = new ArrayList<>();
//            messages.add(new Message("system", "You are a helpful assistant."));
//            messages.add(new Message("user", userMessage)); // Thông điệp người dùng nhập
//
//            // Gửi tin nhắn đến API của ChatGPT và nhận phản hồi
//            ChatGptApi api = RetrofitClientInstance.getRetrofitInstance().create(ChatGptApi.class);
//
//            // Kiểm tra mô hình và gửi yêu cầu
//            String model = "gpt-3.5-turbo"; // Sử dụng mô hình bạn muốn
//            ChatGptRequest request = new ChatGptRequest(model, messages);
//
//            // Ghi log để kiểm tra thông tin yêu cầu
//            Log.d("ChatActivity", "Sending request with model: " + model);
//            Log.d("ChatActivity", "Messages: " + new Gson().toJson(messages)); // Ghi lại thông điệp dưới dạng JSON
//            Log.d("ChatActivity", "Request Body: " + new Gson().toJson(request)); // Ghi lại yêu cầu
//
//            api.sendMessage(request).enqueue(new Callback<ChatGptResponse>() {
//                @Override
//                public void onResponse(Call<ChatGptResponse> call, Response<ChatGptResponse> response) {
//                    if (response.isSuccessful() && response.body() != null) {
//                        String reply = response.body().getChoices().get(0).getMessage().getContent();
//                        // Hiển thị phản hồi từ ChatGPT
//                        Log.e("reply", reply);
//                        TextView replyView = new TextView(ChatActivity.this);
//                        replyView.setText("ChatGPT: " + reply);
//                        messageContainer.addView(replyView);
//                    } else {
//                        Log.e("ChatActivity", "Response not successful: " + response.code() + " - " + response.message());
//                        try {
//                            String errorBody = response.errorBody().string();
//                            Log.e("ChatActivity", "Error body: " + errorBody);
//                        } catch (IOException e) {
//                            Log.e("ChatActivity", "Error parsing error body: " + e.getMessage());
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ChatGptResponse> call, Throwable t) {
//                    Log.e("ChatActivity", "Error: " + t.getMessage());
//                }
//            });
//
//            // Xóa nội dung của EditText
//            messageInput.setText("");
//        }
//    }
//
//}
