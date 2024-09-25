
var ConsultarDoctoServicoNaoFaturadoView =  { 
	name : "consultarDoctoServicoNaoFaturado", 
	title : "consultarDoctoServicoNaoFaturado", 
	controller : ConsultarDoctoServicoNaoFaturadoController, 
	tabs : [ { 
		name : "list", 
		title : "listagem", 
		url : "/", 
		listTab: true 
	}] 
}; 
 
ContasAReceberRotas.views.push(ConsultarDoctoServicoNaoFaturadoView); 
