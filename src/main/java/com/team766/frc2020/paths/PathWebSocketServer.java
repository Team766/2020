package com.team766.frc2020.paths;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.ArrayList;

import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.java_websocket.WebSocket;

import org.json.JSONObject;

import com.team766.framework.Subroutine;
import com.team766.frc2020.Robot;
import com.team766.frc2020.commands.PathRunner;


public class PathWebSocketServer extends WebSocketServer {
    ArrayList<Waypoint> path;
	public PathWebSocketServer(InetSocketAddress address, ArrayList<Waypoint> path) {
        super(address);
        this.path = path;
    }
    
    public PathWebSocketServer(InetSocketAddress address) {
        super(address);
	}
    // im not sure if it is good practice to use json in the body of websockets but it seems to work..

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		// conn.send("Welcome to the server!"); //This method sends a message to the new client
		// broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
		System.out.println("new connection to " + conn.getRemoteSocketAddress());
        broadcastPath(path);
    }

    public void broadcastPath(ArrayList<Waypoint> path) {
        this.path = path;
        String pathString = "";
        pathString += "{\"waypoints\": [";
        
        for (int i = 0; i < path.size(); i++) {
			pathString += "{\"x\": " + Math.round(path.get(i).getX() * 100) / 100.0 + ", \"y\": " + Math.round(path.get(i).getY() * 100) / 100 + "},";
		}
		pathString = pathString.substring(0, pathString.length() - 1) + "]}";

        System.out.println("path string " + pathString);
        broadcast(pathString);
    }

    public void broadcastPosition(double xPosition, double yPosition) {
        broadcast("{\"position\": { \"x\": " + xPosition + ", \"y\": " + yPosition + "}}" );
    }

    public void broadcastHeading(double heading) {
        broadcast("{\"heading\": " + heading + "}" );
    }
    
    public void broadcastClosestPoint(double x, double y) {
        broadcast("{\"closestPoint\": { \"x\": " + x + ", \"y\": " + y + "}}" );
    }
    
    public void broadcastLookaheadPoint(double x, double y) {
        broadcast("{\"lookaheadPoint\": { \"x\": " + y + ", \"y\": " + y + "}}" );
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
		double xPosition, yPosition;
		try {
            JSONObject goalPoint = new JSONObject(message);
            xPosition = goalPoint.getJSONObject("targetPosition").getDouble("xPosition");
			yPosition = goalPoint.getJSONObject("targetPosition").getDouble("yPosition");
			// System.out.println("received target point: x: " + xPosition + " y: " + yPosition);
			
			// callSubroutine(new PathRunner(false, Robot.drive.getGyroAngle(), AStarGeneration.AStarGeneratePathWaypoints(xPosition, yPosition))); // TODO: fix!

        } catch (Exception e) {
            System.out.println(e);
        }		
	}

	@Override
	public void onMessage(WebSocket conn, ByteBuffer message) {
		System.out.println("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
	}
	
	@Override
	public void onStart() {
		System.out.println("path web socket server started successfully");
	}
}