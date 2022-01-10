package com.springclifftop.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class BaseAPI {
    /**
     * 临时凑合一下
     */
    final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36 Edg/94.0.992.38";


    /**
     * 向服务器发送Post请求
     * @param data
     * @param baseURL
     * @param uri
     * @return
     * @throws URISyntaxException
     * @throws IOException
     * @throws InterruptedException
     */
    public static String sendPostRequest(String data , String baseURL, String uri) throws URISyntaxException, IOException, InterruptedException {
        return sendPostRequest(data,baseURL + uri);
    }

    public static String sendPostRequest(String data,String URL) throws IOException, InterruptedException, URISyntaxException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest.BodyPublisher publisher = getPublisher(data);
        HttpRequest requet = HttpRequest.newBuilder()
                .uri(new URI(URL))
                .header("Content-Type","application/json; charset=utf-8")
                .POST(publisher)
                .build();
        HttpResponse<String> response
                = client.send(requet,HttpResponse.BodyHandlers.ofString());
        return response.body();
    }


    public static String sendPostRequest(String data , String baseURL, String uri,String userAgent,String xToken) throws URISyntaxException, IOException, InterruptedException {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();
        HttpRequest.BodyPublisher publisher = getPublisher(data);
        HttpRequest requet = HttpRequest.newBuilder()
                .uri(new URI(baseURL+uri))
                .header("Content-Type","application/json")
                .header("User-Agent",userAgent)
                .header("X-Auth-Token",xToken) //语雀必须的部分,之后考虑如何解决该问题. 可以单独有一个函数专门用于返回 HttpRequest
                .POST(publisher)
                .build();
        HttpResponse<String> response
                = client.send(requet,HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static String sendGetRequest(String baseURL, String uri) throws Exception {
        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();
        String contentType = "application/json";
        HttpRequest requet = HttpRequest.newBuilder()
                .uri(new URI(baseURL+uri))
                .header("Content-Type",contentType)
                .GET()
                .build();
        HttpResponse<String> response
                = client.send(requet,HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    /**
     * 获取BodyPublisher
     * @param data
     * @return
     */
    public static HttpRequest.BodyPublisher getPublisher(String data){
        return HttpRequest.BodyPublishers.ofString(data);
    }


}
