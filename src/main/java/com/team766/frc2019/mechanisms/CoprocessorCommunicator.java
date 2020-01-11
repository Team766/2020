package com.team766.frc2019.mechanisms;

import java.net.http.*;
import java.net.URI;


public class CoprocessorCommunicator {
    private String serverIpAddress;
    /**
     * @param ipAddress the ip address of the pi
     */
    public CoprocessorCommunicator(String serverIpAddress) {
        this.serverIpAddress = serverIpAddress;
    }
    public String getipAddress() {
        return this.serverIpAddress;
    }
    public void get() throws Exception {
        String uri = this.serverIpAddress + ":8000/test";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
              .uri(URI.create(uri))
              .build();
    
        HttpResponse<String> response =
              client.send(request, HttpResponse.BodyHandlers.ofString());
    
        System.out.println(response.body());
    }
}