package main.java.com.hit.server;

/**

 */
public class Request {
    private String action;
    private String payload;

    public Request(String action, String payload) {
        this.action = action;
        this.payload = payload;
    }

    public String getAction() {
        return action;
    }

    public String getPayload() {
        return payload;
    }
}

