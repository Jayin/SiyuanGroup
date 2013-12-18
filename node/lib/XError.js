module.exports = (function(E){
	function Error(msg, code){
		E.apply(this, arguments);
		this.message = msg;
		this.code = code;
	}
	Error.prototype = new E();
	return Error;
})(Error);