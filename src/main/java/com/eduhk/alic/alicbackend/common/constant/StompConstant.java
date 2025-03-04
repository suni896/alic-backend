package com.eduhk.alic.alicbackend.common.constant;

/**
 * @author FuSu
 * @date 2025/3/3 14:14
 */
public enum StompConstant {

    /**
     * STOMP的节点
     */
    STOMP_ENDPOINT("/ws"),
    /**
     * 广播式
     */
    STOMP_TOPIC("/topic"),
    /**
     * 一对一式
     */
    STOMP_USER ("/user"),

    STOMP_APP("/app"),
    /**
     * 单用户消息订阅地址
     */
    SUB_USER ("/chat"),
    /**
     * 单用户消息发布地址
     */
    PUB_USER("/chat"),
    /**
     * 聊天室消息发布地址
     */
    PUB_CHAT_ROOM("/chatRoom"),

    /**
     * 聊天室消息订阅地址
     */
    SUB_CHAT_ROOM("/topic/chatRoom"),
    /**
     * 异常消息订阅地址
     */
    SUB_ERROR ("/error"),
    /**
     * 用户上下线状态消息订阅地址
     */
    SUB_STATUS ("/topic/status"),
    /**
     * 聊天室消息撤消
     */
    PUB_CHAT_ROOM_REVOKE("/chatRoom/revoke");

    private String path;
    StompConstant(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }
}
