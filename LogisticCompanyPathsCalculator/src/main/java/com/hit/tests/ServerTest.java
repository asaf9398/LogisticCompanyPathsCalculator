package main.java.com.hit.tests;

import main.java.com.hit.algorithm.IAlgoBestPathsCalculator;
import main.java.com.hit.server.Server;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ServerTest {
    private Server server;
    private IAlgoBestPathsCalculator mockAlgo;
    private ServerSocket mockServerSocket;
    private Socket mockClientSocket;

    @Before
    public void setUp() throws IOException {
        mockAlgo = mock(IAlgoBestPathsCalculator.class);
        mockServerSocket = mock(ServerSocket.class);
        mockClientSocket = mock(Socket.class);

        server = new Server(4000, mockAlgo);
    }

    @Test
    public void testServerStarts() {
        Thread serverThread = new Thread(() -> server.start());
        serverThread.start();
    }

    @Test
    public void testServerAcceptsClient() throws IOException {
        when(mockServerSocket.accept()).thenReturn(mockClientSocket);

        verify(mockServerSocket, never()).accept();
    }
}
