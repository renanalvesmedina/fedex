var demonstrativoPagamentoComissaoView =  {
	name : "demonstrativoPagamentoComissao",
	title : "demonstrativoPagamentoComissao",
	controller : demonstrativoPagamentoComissaoController,
	tabs : [ {
		name : "list",
		title : "relatorio",
		url : "/",
		listTab: true
	} ]

};

VendasRotas.views.push(demonstrativoPagamentoComissaoView);
