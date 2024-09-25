var ManterClienteTerritorioView =  {
	name : "manterClienteTerritorio",
	title : "cadastroClienteTerritorio",
	controller : ManterClienteTerritorioController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab: true
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		editTab: true,
		controller: ManterClienteTerritorioCadController
	}, {
		name: "transf",
		title: "transferenciaTotalCarteira",
		url: "/transf/:id",
		disabled : false,
		controller: ManterClienteTerritorioTransferenciaCarteiraController
	}, {
		name: "fluxo",
		title: "fluxoAprovacao",
		url: "/fluxo/:id",
		disabled : false,
		controller: ManterClienteTerritorioFluxoAprovacaoController
	} ]
};

VendasRotas.views.push(ManterClienteTerritorioView);
