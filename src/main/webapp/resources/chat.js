// Websocket Endpoint url
var host = window.location.host;
var path = window.location.pathname;
var webCtx = path.substring(0, path.indexOf('/', 1));
var endPointURL = "ws://" + window.location.host + webCtx + "/chat";


var chatClient = null;

function connect() {
    chatClient = new WebSocket(endPointURL);
    chatClient.onmessage = function (event) {
        var jsonObj = JSON.parse(event.data);
        var message = jsonObj.user + ": " + jsonObj.message;
        writeToScreen('RESPONSE: ' + message);
    };
    chatClient.onopen = function (event) {
        onOpen();
    };
    chatClient.onerror = function (event) {
        onError(event);
    };


}
function onError(evt) {
    writeToScreen('ERROR: ' + evt.data);
}

function onOpen() {
    writeToScreen("Connected... " + endPointURL);
}

function writeToScreen(message) {
    var messagesArea = document.getElementById("messages");
    messagesArea.value = messagesArea.value + message + "\r\n";
    messagesArea.scrollTop = messagesArea.scrollHeight;
}

function disconnect() {
    chatClient.close();
}

function sendMessage() {
    var user = document.getElementById("userName").value.trim();
    if (user === "")
        alert("Please enter your name!");

    var inputElement = document.getElementById("messageInput");
    var message = inputElement.value.trim();
    if (message !== "") {
        var jsonObj = {"user": user, "message": message};
        var m = JSON.stringify(jsonObj);
        chatClient.send(m);
        writeToScreen("SEND: " + m);
        inputElement.value = "";
    }
    inputElement.focus();
}