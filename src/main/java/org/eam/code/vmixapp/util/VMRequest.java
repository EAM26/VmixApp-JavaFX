package org.eam.code.vmixapp.util;

import org.eam.code.vmixapp.model.Scene;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class VMRequest {

    private final String ipAddress;
    private final String port;

    private HttpClient httpClient;

    public VMRequest() {
        this.ipAddress = SelectedSequence.getSelectedSequence().getIpAddress();
        this.port = SelectedSequence.getSelectedSequence().getPort();
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPreview(Scene scene) {
        String encodedName = URLEncoder.encode(scene.getName(), StandardCharsets.UTF_8);
        String url = "http://" + ipAddress + ":" + port + "/api/?Function=PreviewInput&Input=" + encodedName;
        sendRequest(url);
    }

    public void cut() {
        String url = "http://" + ipAddress + ":" + port + "/api/?Function=CutDirect";
        sendRequest(url);
    }


    public void sendRequest(String url) {
        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            System.out.println("The url for the request is: " + url);

            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if(response.statusCode() == 200) {
                System.out.println("Request successful.");
            } else {
                System.out.println("Request failed with status: "+ response.statusCode());
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
