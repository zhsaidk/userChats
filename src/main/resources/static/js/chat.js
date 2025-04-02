let socket;
function connect() {
    socket = new WebSocket("ws://localhost:8080/ws/chat");

    socket.onopen = () => {
        console.log("✅ Соединение установлено");
    };

    socket.onmessage = (event) => {
        let msg = document.createElement("li");
        msg.textContent = event.data;
        msg.className = "other-message";
        document.getElementById("messages").appendChild(msg);
        scrollToBottom();
    };

    socket.onerror = (error) => {
        console.error("❌ Ошибка WebSocket:", error);
    };
}

function sendMessage() {
    let input = document.getElementById("messageInput");
    let message = input.value.trim();

    if (message && socket.readyState === WebSocket.OPEN) {
        socket.send(message);
        let msg = document.createElement("li");
        msg.textContent = message;
        msg.className = "my-message";
        document.getElementById("messages").appendChild(msg);
        input.value = "";
        scrollToBottom();
    }
}

document.getElementById("messageInput").addEventListener("keypress", function(event) {
    if (event.key === "Enter") {
        sendMessage();
    }
});

function scrollToBottom() {
    let messages = document.getElementById("messages");
    messages.scrollTop = messages.scrollHeight;
}

connect();
