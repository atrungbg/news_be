package com.fpt.hungnm.assigmentfinal.ChatGptApi;

import java.util.List;

public class ChatGptRequest {
    private String model;
    private List<Message> messages;

    // Constructor
    public ChatGptRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }

    // Lớp lồng bên trong Message
    public static class Message {
        private String role;
        private String content;

        // Constructor
        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }

        public String getRole() {
            return role;
        }

        public String getContent() {
            return content;
        }
    }
}
