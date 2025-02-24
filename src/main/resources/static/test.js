const socket = new SockJS("https://localhost:443/ws");
const stompClient = Stomp.over(socket);

stompClient.connect({}, () => {
    stompClient.subscribe("/topic/group/1", (message) => {
        console.log("收到消息：", JSON.parse(message.body));
    });

    stompClient.send("/app/chat/1", {}, JSON.stringify({
        senderId: 123,
        content: "Hello, world!"
    }));
});