package org.eam.code.vmixapp.util;

import org.eam.code.vmixapp.model.Scene;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class VMRequest {

    private final String ipAddress;
    private final String port;

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
        System.out.println("Preview url: " + url + "  camname is: " + camName);
//        sendRequest(url);
    }



    public void cut() {
        String url = "http://" + ipAddress + ":" + port + "/api/?Function=CutDirect";
//        String url = "https://jsonplaceholder.typicode.com/posts/1";
        System.out.println("Actual url: " + url);
//        sendRequest(url);
    }


    public void sendRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                System.out.println("Request successful.");
                System.out.println("Headers: " + response.headers());
                System.out.println("Body: " + response.body());
            } else {
                System.out.println("Request failed with status: "+ response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }



}
