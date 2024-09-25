
var ManterRecibosDeOutrasEmpresasView = {
	name : "manterRecibosDeOutrasEmpresas",
	title : "manterRecibosDeOutrasEmpresas",
	controller : ManterRecibosDeOutrasEmpresasController,
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
		controller: ManterRecibosDeOutrasEmpresasCadController
	}]
};

FreteCarreteiroViagemRotas.views.push(ManterRecibosDeOutrasEmpresasView);

