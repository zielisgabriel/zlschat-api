package br.com.gabriel.zlschat.enums;

public enum ChatRoomType {
    PRIVATE("private"),
    GROUP("group");

    private String chatRoomType;

    ChatRoomType(String chatRoomType) {
        this.chatRoomType = chatRoomType;
    }

    public String getChatRoomType() {
        return this.chatRoomType;
    }
}
