var ws = new WebSocket(
    "ws://" + window.location.host + "/websocket");

ws.onmessage = function(message) {
    console.log(message);
    document.write("<p>" + message.data + "</p>");
};

ws.onopen = function() {
    //ws.send(JSON.stringify({foo: 'bar'}))
        //ws.send("test")

}

$(window).on('beforeunload', function(){
    ws.close();
});

$(document).ready(function() {


    $('.node-plant-selector').change(function() {
        console.log($(this).val());
        ws.send(JSON.stringify({
            action: "update plant id of node",
            plantId: parseInt($(this).val()),
            nodeId: parseInt($(this).data('node-id'))
        }))
    })

})




