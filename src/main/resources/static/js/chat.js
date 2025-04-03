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

// Функция подгрузки следующей страницы
function loadMoreMessages() {
    const loadMoreButton = document.getElementById("loadMore");
    const currentPage = parseInt(loadMoreButton.getAttribute("data-page"));
    const size = parseInt(loadMoreButton.getAttribute("data-size"));
    const chatId = loadMoreButton.getAttribute("data-chat-id");

    const nextPage = currentPage + 1;

    fetch(`/chat/messages?chatId=${chatId}&page=${nextPage}&size=${size}`)
        .then(response => response.json())
        .then(messages => {
            const messagesList = document.getElementById("messages");
            messages.forEach(msg => {
                let li = document.createElement("li");
                li.textContent = msg.text; // Предполагается, что у Message есть поле text
                li.className = "other-message";
                messagesList.insertBefore(li, messagesList.firstChild); // Добавляем в начало списка
            });
            // Обновляем номер страницы на кнопке
            loadMoreButton.setAttribute("data-page", nextPage);
            // Если сообщений меньше, чем size, скрываем кнопку
            if (messages.length < size) {
                loadMoreButton.style.display = "none";
            }
        })
        .catch(error => console.error("Ошибка при загрузке сообщений:", error));
}

// Инициализация
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll("#messages li").forEach((msg) => {
        msg.classList.add("other-message");
    });
    scrollToBottom();

    // Привязываем событие к кнопке "Загрузить ещё"
    document.getElementById("loadMore").addEventListener("click", loadMoreMessages);
});

function scrollToBottom() {
    let messages = document.getElementById("messages");
    messages.scrollTop = messages.scrollHeight;
}

connect();