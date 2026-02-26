package info.jab.http;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestDemo {

    private final HttpClient httpClient;

    public RequestDemo() {
        this.httpClient = HttpClient.newHttpClient();
    }

    public RequestDemo(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public String fetchData(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(url))
            .header("Content-Type", "application/json")
            .GET()
            .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        RequestDemo demo = new RequestDemo();
        String result = demo.fetchData("https://api.example.com/data");
        System.out.println(result);
    }
}
