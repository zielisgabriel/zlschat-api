package br.com.gabriel.zlschat.enums;

public enum MessageStatus {
    SENDING("SENDING"),
    SENT("SENT"),
    READ("READ");

    private String status;

    MessageStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
