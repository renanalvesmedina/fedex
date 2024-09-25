function ignore() {
	return false;
}

function openModalDialog(url, width, height) {
	return showModalDialog(url, window, 'unardorned:no;scroll:auto;resizable:no;status:no;center=yes;help:no;dialogWidth:' + width + 'px;dialogHeight:' + height + 'px;');
}

function clearElement(obj, doc) {
	if(obj == undefined) {
		return;
	}
	if(obj != '[object]') {
		if(doc == undefined) {
			doc = this.document;
		}
		obj = doc.getElementById(obj);
	}
	resetValue(obj);
	obj.options.length = 1;
}

function displayWarnings(data) {
	if(data) {
		if(data.warnings && data.warnings.length > 0) {
			for(var i = 0; i < data.warnings.length; i++) {
				alert(data.warnings[i].warning);
			}
		}
	}
}