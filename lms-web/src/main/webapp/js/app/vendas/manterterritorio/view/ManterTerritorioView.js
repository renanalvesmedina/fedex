var ManterTerritorioView =  {
	name : "manterTerritorio",
	title : "cadastroTerritorio",
	controller : ManterTerritorioController,
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
		controller: ManterTerritorioCadController
	}, {
		name : "equipeVendas",
		title : "equipeVendas",
		url : "/equipeVendas/:id",
		disabled: "!data.id",
		controller: ManterTerritorioEquipeVendasController
	}, {
		name : "recalculoComissao",
		title : "recalculoComissao",
		url : "/recalculoComissao/:id",
		disabled: "!data.id",
		controller: ManterTerritorioRecalculoComissaoController
	}]
};

VendasRotas.views.push(ManterTerritorioView);
