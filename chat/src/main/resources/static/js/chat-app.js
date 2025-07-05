'use strict';

document.addEventListener('DOMContentLoaded', () => {
    const messageForm = document.querySelector('#message-form');
    const messageInput = document.querySelector('#message');
    const messageArea = document.querySelector('#message-area');
    const connectingElement = document.querySelector('#connecting');
    const statusIndicator = document.querySelector('#professor-status');
    const imageUploadInput = document.querySelector('#image-upload-input');

    let stompClient = null;
    let username = document.querySelector('#username').innerText.trim();
    let duvidaId = document.querySelector('#duvida-id').innerText.trim();

    function connect() {
        if (username && duvidaId) {
            const socket = new SockJS('/ws-chat');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, onConnected, onError);
        }
    }

    function onConnected() {
        stompClient.subscribe('/topic/chat/' + duvidaId, onMessageReceived);
        loadChatHistory();
        stompClient.send(`/app/chat.addUser/${duvidaId}`, {}, JSON.stringify({ remetente: username, tipo: 'JOIN' }));
        connectingElement.classList.add('d-none');
        messageForm.classList.remove('d-none');
    }

    function onError(error) {
        connectingElement.textContent = 'Não foi possível conectar ao chat.';
        connectingElement.style.color = 'red';
    }

    async function loadChatHistory() {
        try {
            const response = await fetch(`/chat/historico/${duvidaId}`);
            if (!response.ok) throw new Error('Falha ao carregar histórico');
            const messages = await response.json();
            messages.forEach(displayMessage);
        } catch (error) {
            console.error(error);
        }
    }

    function sendMessage(event) {
        event.preventDefault();
        const messageContent = messageInput.value.trim();
        if (messageContent && stompClient) {
            const chatMessage = { remetente: username, conteudo: messageContent, tipo: 'CHAT' };
            stompClient.send(`/app/chat.sendMessage/${duvidaId}`, {}, JSON.stringify(chatMessage));
            messageInput.value = '';
        }
    }

    function sendImage(event) {
        const file = event.target.files[0];
        if (file) {
            const formData = new FormData();
            formData.append('file', file);
            formData.append('remetente', username);

            fetch(`/chat/${duvidaId}/upload`, {
                method: 'POST',
                body: formData
            }).catch(error => console.error('Erro no upload da imagem:', error));
        }
        event.target.value = null;
    }

    function onMessageReceived(payload) {
        const message = JSON.parse(payload.body);
        switch (message.tipo) {
            case 'CHAT':
            case 'IMAGE':
                displayMessage(message);
                break;
            case 'PROFESSOR_ONLINE':
                updateProfessorStatus(true);
                break;
            case 'PROFESSOR_OFFLINE':
                updateProfessorStatus(false);
                break;
        }
    }

    function updateProfessorStatus(isOnline) {
        if (!statusIndicator) return;
        statusIndicator.textContent = isOnline ? 'Professor Online' : 'Professor Offline';
        statusIndicator.classList.toggle('online', isOnline);
        statusIndicator.classList.toggle('offline', !isOnline);
    }

    function displayMessage(message) {
        const messageElement = document.createElement('li');
        messageElement.classList.add('chat-message');
        const isMyMessage = (message.remetente === username);
        messageElement.classList.add(isMyMessage ? 'self' : 'other');

        if (!isMyMessage) {
            const senderElement = document.createElement('span');
            senderElement.classList.add('sender');
            senderElement.textContent = message.remetente;
            messageElement.appendChild(senderElement);
        }

        if (message.tipo === 'IMAGE') {
            const img = document.createElement('img');
            img.src = `/chat/message/${message.id}/imagem`;
            img.alt = message.conteudo;
            img.classList.add('chat-image', 'expandable-image-chat');
            messageElement.appendChild(img);
        } else {
            const textElement = document.createElement('p');
            textElement.textContent = message.conteudo;
            if(isMyMessage) {
                textElement.style.color = 'white';
            }
            messageElement.appendChild(textElement);
        }

        messageArea.appendChild(messageElement);
        messageArea.scrollTop = messageArea.scrollHeight;
    }

    connect();
    if (messageForm) messageForm.addEventListener('submit', sendMessage);
    if (imageUploadInput) imageUploadInput.addEventListener('change', sendImage);
});