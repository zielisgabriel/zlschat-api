package br.com.gabriel.zlschat.enums;

public enum UserStatus {
    ONLINE("online"),
    OFFLINE("offline"),
    AWAY("away");

    private String status;

    UserStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
