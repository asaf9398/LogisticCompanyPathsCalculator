package networking;

public class Request {
    public String action;
    public String payload;

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
