package com.example.upath;

public class UpdateProfileResponse {

    public boolean success;
    public Data data;
    public String error;

    public static class Data {
        public String nome;
        public String email;
        public String fotoUrl;
    }
}
