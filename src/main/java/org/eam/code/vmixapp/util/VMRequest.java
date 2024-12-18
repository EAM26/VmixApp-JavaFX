package org.eam.code.vmixapp.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class VMRequest {

    private String ipAddress;
    private String port;

    private HttpClient httpClient;

    public VMRequest(String ipAddress, String port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public void setPreview(String name) {

    }


    public void sendRequest(String vmFunction, String input) {
        if (input == null) {
            throw new IllegalArgumentException("Input can not be null");
        }
        String url = "http://" + ipAddress + ":" + port + "/api/?Function=" + vmFunction + " =" + input;
        System.out.println(url);

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

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
