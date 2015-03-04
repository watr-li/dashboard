//var ws = new WebSocket(
//"ws://" + window.location.host + "/websocket");

var ws = new WebSocket(
    "ws://localhost:9000/websocket");

ws.onmessage = function(message) {
    console.log(message);
    document.write("<p>" + message.data + "</p>");
};

$(document).ready(function() {


    $('.node-plant-selector').change(function() {
        console.log($(this).val());
        ws.send({foo: 'bar'})
    })

})




