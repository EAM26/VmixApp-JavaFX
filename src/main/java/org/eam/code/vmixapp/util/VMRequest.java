package org.eam.code.vmixapp.util;

import org.eam.code.vmixapp.model.Scene;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class VMRequest {

    private final String ipAddress;
    private final String port;
    private static final Logger logger = LoggerFactory.getLogger(VMRequest.class);
    private final HttpClient httpClient;

    public VMRequest() {
        this.ipAddress = SelectedSequence.getSelectedSequence().getIpAddress();
        this.port = SelectedSequence.getSelectedSequence().getPort();
        this.httpClient = HttpClient.newHttpClient();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }



    public void setPreview(Scene scene) {
        String camName = scene.getCamera().getName();

        String url = "http://" + ipAddress + ":" + port + "/api/?Function=PreviewInput&Input=" + camName;
//        String url = "https://jsonplaceholder.typicode.com/posts/1";
        sendRequest(url);
    }



    public void cut() {
        String url = "http://" + ipAddress + ":" + port + "/api/?Function=CutDirect";
//        String url = "https://jsonplaceholder.typicode.com/posts/1";
        sendRequest(url);
    }

    public String getCamsFromVmix() {
        String url = "http://" + ipAddress + ":" + port + "/api/";
//        String url = "https://jsonplaceholder.typicode.com/posts/1";
        sendRequest(url);
        return sendRequest(url);
    }


    public String sendRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(2))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Requenst sended: " + url);
            if(response.statusCode() == 200) {
                System.out.println("Headers: " + response.headers());
//                System.out.println("Body: " + response.body());
                return response.body();
            } else {
                System.out.println("Request failed with status: "+ response.statusCode());
                return "";
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
