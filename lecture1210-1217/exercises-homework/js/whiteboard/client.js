$(document).ready(function(){
    var wtbd=document.getElementById("wtbd");
    var wtbdCtx=wtbd.getContext("2d");

    var socketMsg = io();

    function handleDrev(drev) {
        if (drev instanceof LineEvent) {
            wtbdCtx.beginPath();
            wtbdCtx.moveTo(drev.fromX, drev.fromY);
            wtbdCtx.lineTo(drev.toX, drev.toY);
            wtbdCtx.stroke();

        } else if (drev instanceof EraseEvent) {
            wtbdCtx.clearRect(drev.x - 5, drev.y - 5, 11, 11);
        }
    };

    socketMsg.on('drev', function(drev) {
        drevReassignProto(drev);

        handleDrev(drev);
    });

    wtbd.mouseButtonDown = false;

    var lastX, lastY;
    var currentTool = 0; // 0 - pen, 1 - eraser

    showStatus();

    wtbd.onmousemove = function(evt) {
        var rect = wtbd.getBoundingClientRect();
        var x = evt.clientX - rect.left;
        var y = evt.clientY - rect.top;

        if (wtbd.mouseButtonDown) {
            var drev;

            if (currentTool == 0) {
                drev = new LineEvent(lastX, lastY, x, y);
            } else {
                drev = new EraseEvent(x, y);
            }

            handleDrev(drev);
            socketMsg.emit('drev', drev);
        }

        lastX = x;
        lastY = y;

        showStatus();
    };

    wtbd.onmousedown = function(evt) {
        wtbd.mouseButtonDown = true
        showStatus();
    };

    wtbd.onmouseup = function(evt) {
        wtbd.mouseButtonDown = false
        showStatus();
    };

    function showStatus() {
        $("#log").html('Mouse position: ' + lastX + ',' + lastY +
            '<br/>Button pressed: ' + wtbd.mouseButtonDown +
            '<br/>Selected tool: ' + currentTool);
    }

    $("#tool-pen").click(function() {
        currentTool = 0;
        $("#tool-eraser").removeClass('selectedTool');
        $("#tool-pen").addClass('selectedTool');
        showStatus();
    });

    $("#tool-eraser").click(function() {
        currentTool = 1;
        $("#tool-eraser").addClass('selectedTool');
        $("#tool-pen").removeClass('selectedTool');
        showStatus();
    });
});
