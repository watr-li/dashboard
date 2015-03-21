var ws = new WebSocket(
    "ws://" + window.location.host + "/websocket");

ws.onmessage = function(message) {
    var data = JSON.parse(message.data)

    if(data['action'] === 'update plant overview') {
        $('#plantOverview').replaceWith($(data.html))
    }

    if(data['action'] === 'update node list') {
        $('#nodeList').replaceWith($(data.html))
    }
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




