var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    $("#send").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#msg").html("");
}

function connect() {
    var socket = new SockJS('/ws');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/public', function (message) {
            showMessage(message.body);
        });

        // 연결 후 웰컴 메시지 요청
        stompClient.send("/app/sendMessage", {}, JSON.stringify("open"));
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendMessage() {
    let message = $("#msg").val();
    showMessage("보낸 메시지: " + message);
    stompClient.send("/app/sendMessage", {}, JSON.stringify(message));
}

function showMessage(message) {
    console.log("Received message: " + message);
    let parsedMessage;
    try {
        parsedMessage = JSON.parse(message);
    } catch (e) {
        $("#communicate").append("<tr><td>" + message + "</td></tr>");
        return;
    }

    if (parsedMessage.bubbles) {
        parsedMessage.bubbles.forEach(function(bubble) {
            if (bubble.type === "template") {
                // 템플릿 타입 메시지 처리
                let description = bubble.data.cover ? bubble.data.cover.data.description : "";
                $("#communicate").append("<tr><td>" + description + "</td></tr>");

                let contentTable = bubble.data.contentTable || [];
                let buttonsHtml = "<div class='button-group'>";
                contentTable.forEach(function(row) {
                    row.forEach(function(item) {
                        let buttonTitle = item.data.title;
                        let postback = item.data.data.action.data.postback;
                        buttonsHtml += `<button class='option-button' onclick='handlePostback("${postback}")'>${buttonTitle}</button>`;
                    });
                });
                buttonsHtml += "</div>";
                $("#dynamic-content").append(buttonsHtml);
            } else if (bubble.type === "text") {
                // 일반 텍스트 타입 메시지 처리
                $("#communicate").append("<tr><td>" + bubble.data.description + "</td></tr>");
            }
        });
    }
}



function handlePostback(postback) {
    console.log("Postback received: " + postback);
    stompClient.send("/app/sendMessage", {}, JSON.stringify(postback));
}

$(function () {
    $("form").on('submit', function (e) {
        e.preventDefault();
    });
    $("#connect").click(function () {
        connect();
    });
    $("#disconnect").click(function () {
        disconnect();
    });
    $("#send").click(function () {
        sendMessage();
    });
});
