package com.team766.frc2019.paths;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

public class PathWebSocketServer extends WebSocketServer {
	public PathWebSocketServer(InetSocketAddress address) {
		super(address);
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		conn.send("Welcome to the server!"); //This method sends a message to the new client
		broadcast( "new connection: " + handshake.getResourceDescriptor() ); //This method sends a message to all clients connected
		System.out.println("new connection to " + conn.getRemoteSocketAddress());
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		System.out.println("closed " + conn.getRemoteSocketAddress() + " with exit code " + code + " additional info: " + reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("received message from "	+ conn.getRemoteSocketAddress() + ": " + message);
	}

	@Override
	public void onMessage( WebSocket conn, ByteBuffer message ) {
		System.out.println("received ByteBuffer from "	+ conn.getRemoteSocketAddress());
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		System.err.println("an error occurred on connection " + conn.getRemoteSocketAddress()  + ":" + ex);
	}
	
	@Override
	public void onStart() {
		System.out.println("server started successfully");
	}
}

// import java.io.IOException;
// import java.util.Set;
// import java.util.HashMap;
// import javax.websocket.Session;
// import java.util.concurrent.CopyOnWriteArraySet;
// import javax.websocket.EncodeException;
// import javax.websocket.server.ServerEndpoint;
// import javax.websocket.OnMessage;
// import javax.websocket.OnOpen;

// // @ServerEndpoint("/data")
// // class DataEndpoint {
// //     private Session session;
// //     private static Set<DataEndpoint> dataEndpoints 
// //       = new CopyOnWriteArraySet<>();
// //     private static HashMap<String, String> users = new HashMap<>();

// @ServerEndpoint("/data")
// public class WebSocketEndpoint {
//     @OnMessage
//     public void onMessage(Session session, String msg) {
//         try {   
//             for (Session sess : session.getOpenSessions()) {
//                 if (sess.isOpen())
//                 sess.getBasicRemote().sendText(msg);
//             }
//         } catch (IOException e) {}
//     }

//     @OnOpen
//     public void onOpen(Session session) {
//         try {   
//             for (Session sess : session.getOpenSessions()) {
//                 if (sess.isOpen())
//                 sess.getBasicRemote().sendText("client connected");
//             }
//         } catch (IOException e) {}
//         System.out.println("onOpen::" + session.getId());        
//     }
// }

// //     static void broadcast(Message message)
// //     throws IOException, EncodeException {
// //       dataEndpoints.forEach(endpoint -> {
// //           synchronized (endpoint) {
// //               try {
// //                   endpoint.session.getBasicRemote().
// //                     sendObject(message);
// //               } catch (IOException | EncodeException e) {
// //                   e.printStackTrace();
// //               }
// //           }
// //       });
// //   }
// // }

// // class Message {
// //     public String from;
// //     public String to;
// //     public String content;
    
// //     //standard constructors, getters, setters
// // }
 