package main.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

public class BaseAPI {

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
    public static String sendPostRequest(String data , String baseURL, String uri)
            throws URISyntaxException, IOException, InterruptedException {

        HttpClient client = HttpClient.newBuilder()
                .followRedirects(HttpClient.Redirect.ALWAYS).build();
        String contentType = "application/json";
        HttpRequest.BodyPublisher publisher = getPublisher(data);
        HttpRequest requet = HttpRequest.newBuilder()
                .uri(new URI(baseURL+uri))
                .header("Content-Type",contentType)
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
