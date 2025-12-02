package com.example.upath;

public class LoginResponse {

    public String access_token;
    public String token_type;
    public User user;

    // ---- GETTERS ----
    public String getAccessToken() {
        return access_token;
    }

    public String getTokenType() {
        return token_type;
    }

    public User getUser() {
        return user;
    }

    // ================= USER =================
    public static class User {
        public String id;
        public String nome;
        public String email;
        public String role;
        public String foto_url;

        // GETTERS
        public String getId() { return id; }
        public String getNome() { return nome; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
        public String getFotoUrl() { return foto_url; }
    }
}
