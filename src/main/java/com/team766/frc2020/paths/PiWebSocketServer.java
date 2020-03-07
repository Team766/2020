package com.team766.frc2020.paths;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import com.team766.frc2020.Robot;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;

import org.json.JSONObject;

public class PiWebSocketServer extends WebSocketServer {

    public double xPosition = 0;
	public double yPosition = 0;
	public double theta = 0;
    
    public PiWebSocketServer(InetSocketAddress address) {
        super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// conn.send("Welcome to the server!"); //This method sends a message to the new client
		// broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
		System.out.println("new connection to " + conn.getRemoteSocketAddress());
    }
	
	public void broadcastDeltas(double deltaForward, double deltaTheta) {
		//System.out.println("Sending: " + deltaForward + " " + deltaTheta);
		broadcast("{\"deltas\": { \"forward\": " + deltaForward + ", \"theta\": " + deltaTheta + "}}" );
	}

	public void broadcastPosition(double xPosition, double yPosition, double theta) {
		broadcast("{\"position\": { \"xPosition\": " + xPosition + ", \"yPosition\": " + yPosition + ", \"theta\": " + theta + "}}" );
	}

	public void broadcastDeltaPosition(double deltaX, double deltaY, double deltaTheta) {
		broadcast("{\"deltaPosition\": { \"deltaX\": " + deltaX + ", \"deltaY\": " + deltaY + ", \"deltaTheta\": " + deltaTheta + "}}" );
	}

    @Override
    public void broadcast(String text) {
        super.broadcast(text);
    }

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
        //System.out.println("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
        try {
            JSONObject pmessage = new JSONObject(message);
            // xPosition = Double.parseDouble(pmessage.getJSONObject("position").getString("x"));
			// yPosition = Double.parseDouble(pmessage.getJSONObject("position").getString("y"));
			// theta = Double.parseDouble(pmessage.getJSONObject("position").getString("theta"));
            Robot.drive.setXPosition(Double.parseDouble(pmessage.getJSONObject("position").getString("x")));
            Robot.drive.setYPosition(Double.parseDouble(pmessage.getJSONObject("position").getString("y")));
        } catch (Exception e) {
            //System.out.println(e);
        }
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		System.out.println("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		//System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
	}
	
	@Override
	public void onStart() {
		System.out.println("server started successfully");
    }

    public double getXPosition() {
        return xPosition;
    }

    public double getYPosition() {
        return yPosition;
    }
}