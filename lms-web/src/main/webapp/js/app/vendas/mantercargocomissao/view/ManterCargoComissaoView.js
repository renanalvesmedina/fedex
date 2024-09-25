var ManterCargoComissaoView =  {
	name : "manterCargoComissao",
	title : "manterCargoComissao",
	controller : ManterCargoComissaoController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab: true
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		editTab: true
	} ]
};

VendasRotas.views.push(ManterCargoComissaoView);
