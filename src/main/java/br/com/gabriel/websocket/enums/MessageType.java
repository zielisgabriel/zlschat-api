package br.com.gabriel.websocket.enums;

public enum MessageType {
    PRIVATE("private"),
    GROUP("group");

    private String messageType;

    MessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getMessageType() {
        return this.messageType;
    }
}
