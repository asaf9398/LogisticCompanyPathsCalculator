package client;

import com.google.gson.Gson;
import networking.Request;
import networking.Response;

import java.io.*;
import java.net.Socket;

public class Client {
    private String serverAddress;
    private int serverPort;
    private Socket socket;
    private PrintWriter output;
    private BufferedReader input;
    private Gson gson;

    public Client(String serverAddress, int serverPort) {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.gson = new Gson();
    }

    public Client(String serverAddress, int serverPort, Socket socket) throws IOException {
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
        this.gson = new Gson();
        this.socket = socket;
        this.output = new PrintWriter(socket.getOutputStream(), true);
        this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void connect() throws IOException {
        socket = new Socket(serverAddress, serverPort);
        output = new PrintWriter(socket.getOutputStream(), true);
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public Response sendRequest(Request request) throws IOException {
        String jsonRequest = gson.toJson(request);
        output.println(jsonRequest);

        String jsonResponse = input.readLine();
        if (jsonResponse == null) {
            return new Response(false, "No response from server");
        }
        return gson.fromJson(jsonResponse, Response.class);
    }

    public void close() throws IOException {
        if (socket != null) socket.close();
        if (output != null) output.close();
        if (input != null) input.close();
    }
}
