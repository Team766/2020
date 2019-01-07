package com.team766.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/*
 * Creates an HTTP Server on the robot that can change the 
 * settings of it.  It is initially just used to change robot
 * values.  Hopefully this will be later updated to support 
 * graphing and even small changes in the robot's actual code.
 * 
 * Can be reached at:
 * 	roboRio-766.local:5800/values
 */

public class WebServer {
	
	public interface Handler {
		String title();
		String handle(Map<String, Object> params);
	}

	private HttpServer server;
	private HashMap<String, Handler> pages = new HashMap<>();
	
	public void addHandler(String endpoint, Handler handler) {
		pages.put(endpoint, handler);
	}

	public void start(){
		try {
			server = HttpServer.create(new InetSocketAddress(5800), 0);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		for (Map.Entry<String, Handler> page : pages.entrySet()) {
			HttpHandler httpHandler = new HttpHandler() {
				@Override
				public void handle(HttpExchange exchange) throws IOException {
					Map<String, Object> params = parseParams(exchange);
					String response = "<!DOCTYPE html><html><body>";
					response += buildPageHeader();
					try {
						response += page.getValue().handle(params);
					} catch (Exception ex) {
						ex.printStackTrace();
						throw ex;
					}
					response += "</body></html>";
					exchange.sendResponseHeaders(200, response.getBytes().length);
					try (OutputStream os = exchange.getResponseBody()) {
                        os.write(response.getBytes());
                    }
				}
			};
			server.createContext(page.getKey(), httpHandler);
		}
		server.start();
	}
	
	protected String buildPageHeader() {
		String result = "<p>\n";
		for (Map.Entry<String, Handler> page : pages.entrySet()) {
			result += String.format("<a href=\"%s\">%s</a><br>\n", page.getKey(), page.getValue().title());
		}
		result += "</p>\n";
		return result;
	}
	
    public Map<String, Object> parseParams(HttpExchange exchange) throws IOException {
    	Map<String, Object> parameters = new HashMap<String, Object>();
        parseGetParameters(exchange, parameters);
        parsePostParameters(exchange, parameters);
		return parameters;
    }    

    private void parseGetParameters(HttpExchange exchange, Map<String, Object> parameters)
        throws UnsupportedEncodingException {
        URI requestedUri = exchange.getRequestURI();
        String query = requestedUri.getRawQuery();
        parseQuery(query, parameters);
    }

    private void parsePostParameters(HttpExchange exchange, Map<String, Object> parameters)
        throws IOException {

        if ("post".equalsIgnoreCase(exchange.getRequestMethod())) {
            InputStreamReader isr =
                new InputStreamReader(exchange.getRequestBody(), "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String query = br.readLine();
            parseQuery(query, parameters);
        }
    }

    private void parseQuery(String query, Map<String, Object> parameters)
        throws UnsupportedEncodingException {

        if (query != null) {
            String pairs[] = query.split("[&]");

            for (String pair : pairs) {
                String param[] = pair.split("[=]");

                String key = null;
                String value = null;
                if (param.length > 0) {
                    key = URLDecoder.decode(param[0], System.getProperty("file.encoding"));
                }

                if (param.length > 1) {
                    value = URLDecoder.decode(param[1], System.getProperty("file.encoding"));
                }

                if (parameters.containsKey(key)) {
                    Object obj = parameters.get(key);
                    if(obj instanceof List<?>) {
                        @SuppressWarnings("unchecked")
						List<String> values = (List<String>)obj;
                        
                        values.add(value);
                    } else if(obj instanceof String) {
                        List<String> values = new ArrayList<String>();
                        values.add((String)obj);
                        values.add(value);
                        parameters.put(key, values);
                    }
                } else {
                    parameters.put(key, value);
                }
            }
        }
    }
}
