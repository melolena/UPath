package com.example.upath;

public class UpdateProfileResponse {
    public boolean success;
    public Data data;

    public static class Data {
        public String nome;
        public String email;
        public String fotoUrl;
    }
}
