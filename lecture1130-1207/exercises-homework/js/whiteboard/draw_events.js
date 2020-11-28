LineEvent = function(fromX, fromY, toX, toY) {
	this.fromX = fromX;
	this.fromY = fromY;
	this.toX = toX;
	this.toY = toY;
	this.type = 'line';
}

LineEvent.prototype = {
		toString: function() {
			return 'line: (' + this.fromX + ',' + this.fromY + ') -> (' + this.toX + ',' + this.toY + ')'
		}
}

EraseEvent = function(x, y) {
	this.x = x;
	this.y = y;
	this.type = 'erase';
}

EraseEvent.prototype = {
		toString: function() {
			return 'erase: (' + this.x + ',' + this.y + ')'
		}
}

drevReassignProto = function(drev) {
	if (drev.type == 'line') {
		drev.__proto__ = LineEvent.prototype;
	} else if (drev.type == 'erase') {
		drev.__proto__ = EraseEvent.prototype;		
	}
}

