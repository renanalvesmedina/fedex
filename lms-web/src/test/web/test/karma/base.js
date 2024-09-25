var goog = {require: function(){}, provide: function(){}}; 

var contextPath = '/';
var systemName = '<%=systemName%>';
var version = '<%=version%>';
var serverInfo = '<%=serverInfo%>';
var v = '<%=v%>';
var Data = {
    preferredLanguage : 'pt_br'
};
var isSwt = window.location.href.indexOf("swt") > 0;
/*
* Funcao de retorno para o objeto Browser do SWT.
*/
function openLmsSwt(url){
        invokeLmsSwt(url);
}
