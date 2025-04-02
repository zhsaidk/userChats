let socket;
let reconnectInterval = 3000;

// Получаем chatId из URL
const urlParams = new URLSearchParams(window.location.search);
const chatId = urlParams.get("chatId");

function connect() {
    if (!chatId) {
        console.error("❌ chatId не указан в URL");
        return;
    }

    socket = new WebSocket(`ws://localhost:8080/ws/chat?chatId=${chatId}`);

    socket.onopen = () => {
        console.log(`✅ Соединение установлено для комнаты ${chatId}`);
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

    socket.onclose = () => {
        console.warn(`⚠️ Соединение закрыто для комнаты ${chatId}. Переподключение через ${reconnectInterval / 1000} секунд...`);
        setTimeout(connect, reconnectInterval);
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