package com.saeefmd.movieapp.NetworkUtilities;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtilis {

    String url;

    public NetworkUtilis(String url) {
        this.url = url;
    }

    public String getData() {

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }
}
