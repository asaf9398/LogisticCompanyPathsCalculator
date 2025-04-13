package tests;

import client.Client;
import com.google.gson.Gson;
import networking.Request;
import networking.Response;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ClientTest {
    private Client client;
    private Socket mockSocket;
    private PrintWriter mockOutput;
    private BufferedReader mockInput;
    private PipedOutputStream pipedOutputStream;
    private PipedInputStream pipedInputStream;
    private Gson gson;

    @Before
    public void setUp() throws IOException {
        mockSocket = mock(Socket.class);
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream = new PipedInputStream(pipedOutputStream);

        mockOutput = new PrintWriter(pipedOutputStream, true);
        mockInput = new BufferedReader(new InputStreamReader(pipedInputStream));
        gson = new Gson();

        when(mockSocket.getOutputStream()).thenReturn(pipedOutputStream);
        when(mockSocket.getInputStream()).thenReturn(pipedInputStream);

        client = new Client("localhost", 8080, mockSocket);
    }

    @Test
    public void testSendRequest_Success() throws IOException {
        Request mockRequest = new Request("testAction", "testData");
        Response mockResponse = new Response(true, "Success");
        String jsonResponse = gson.toJson(mockResponse);

        new Thread(() -> {
            try {
                mockOutput.println(jsonResponse);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        Response response = client.sendRequest(mockRequest);

        assertNotNull(response);
        assertTrue(response.isSuccess());
        assertEquals("Success", response.getMessage());
    }

    @Test
    public void testSendRequest_NoResponse() throws IOException {
        Request mockRequest = new Request("testAction", "testData");

        Response response = client.sendRequest(mockRequest);

        assertNotNull(response);
        assertFalse(response.isSuccess());
        assertEquals(null, response.getMessage());
    }



    @Test
    public void testClose() throws IOException {
        client.close();

        verify(mockSocket, times(1)).close();
    }
}
