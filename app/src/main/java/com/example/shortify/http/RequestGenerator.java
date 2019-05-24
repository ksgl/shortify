package com.example.shortify.http;

import okhttp3.Request;

// RequestGenerator generates API request counting on service selected
abstract public class RequestGenerator {
    String url = "";
    String service = "";

    public RequestGenerator() { }

    public Request GetRequest(String service) {
        String url = "";

        switch (service) {
            case "bit.ly":
                url = "https://api-ssl.bitly.com/v3/shorten?access_token=fc11278ca50671dbd19332c8698026c7a9cd4123&longUrl=";
                break;
            case "cleanuri":
                url = "https://cleanuri.com/api/v1/shorten";
                break;
            default:
                throw new IllegalArgumentException("Incorrect service");
        }

        return new Request.Builder().url(url).build();
    }
}
