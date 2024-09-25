var manterFechamentoComissoesView =  { 
	name : "manterFechamentoComissoes", 
	title : "manterFechamentoComissoes", 
	controller : manterFechamentoComissoesController, 
	tabs : [ { 
		name : "list", 
		title : "fechamento", 
		url : "/", 
		listTab: true 
	}, {
		name: "fluxo",
		title: "fluxoAprovacao",
		url: "/fluxo/:id",
		disabled : "!aprovou",
		controller: manterFechamentoComissoesAbaFluxoController
	} ] 
}; 
 
VendasRotas.views.push(manterFechamentoComissoesView); 
