
var LiberarFechamentoMensalCobrancaView =  { 
	name : "liberarFechamentoMensalCobranca", 
	title : "liberarFechamentoMensalCobranca", 
	controller : LiberarFechamentoMensalCobrancaController, 
	tabs : [ { 
		name : "cad", 
		title : "detalhamento", 
		url : "/", 
		listTab: true 
	}] 
}; 
 
ConfiguracoesRotas.views.push(LiberarFechamentoMensalCobrancaView); 
