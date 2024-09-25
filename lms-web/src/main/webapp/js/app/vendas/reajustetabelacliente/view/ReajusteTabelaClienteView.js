var ReajusteTabelaClienteView = {
	name: "reajusteTabelaCliente",
	title: "reajusteTabelaCliente",
	url: "/reajusteTabelaCliente",
	controller: ReajusteTabelaClienteController,
	tabs: [{
		name: "list",
		title: "listagem",
		url: "/",
		disabled: false,
		listTab: true
	}, {
		name: "cad",
		title: "detalhamento",
		url: "/detalhe/:id",
		disabled : false,
		controller: ReajusteTabelaClienteAbaDetailController,
		editTab: true
	}, {
		name: "reaj",
		title: "reajuste",
		url: "/reajuste/:id",
		disabled : "!$stateParams.id",
		controller: ReajusteTabelaClienteAbaReajusteController
	}, {
		name: "fluxo",
		title: "fluxoAprovacao",
		url: "/fluxo/:id",
		disabled : "!$stateParams.id",
		controller: ReajusteTabelaClienteAbaFluxoController
	}]
};

VendasRotas.views.push(ReajusteTabelaClienteView);
