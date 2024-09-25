
var GerarNotaCreditoPadraoView =  {
	name : "gerarNotaCreditoPadrao",
	title : "gerarNotasCreditoPadrao",
	controller : GerarNotaCreditoPadraoController,
	tabs : [ {
		name : "list",
		title : "gerar",
		url : "/",
		listTab: true
	}]
};

FreteCarreteiroColetaEntregaRotas.views.push(GerarNotaCreditoPadraoView);
