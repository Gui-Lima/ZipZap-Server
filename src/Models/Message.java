package Models;

enum Type {
    CONECT,
    MESSAGE,
    ENDCONECT
}

public class Message {

    private String text;
    private String toIP;
    private String fromIP;
    public Type type;

    public Message() {}

    public String toString() {
        StringBuilder message = new StringBuilder();
        message.append(this.fromIP).append("||").append(this.toIP).append("||").append(this.text);
        return message.toString();
    }

    public void setText(String str) {
        this.text = str;
    }

    public void setToIP(String ip) {
        this.toIP = ip;
    }

    public void setFromIP(String ip) {
        this.fromIP = ip;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getText() {
        return this.text;
    }

    public String getToIP() {
        return this.toIP;
    }

    public String getFromIP() {
        return this.fromIP;
    }

    public Type getType() {
        return this.type;
    }
}
