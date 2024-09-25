
var ManterFornecedorEscoltaView = {
	name : "manterFornecedorEscolta",
	title : "manterFornecedorEscolta",
	controller : ManterFornecedorEscoltaController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab : true
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/:id",
		editTab : true
	}, {
		name : "franquia",
		title : "kmFranquia",
		url : "/:id/franquia",
		disabled : "!$stateParams.id",
		controller : ManterKmFranquiaController
	}, {
		name : "blacklist",
		title : "blacklistClientes",
		url : "/:id/blacklist",
		disabled : "!$stateParams.id",
		controller : ManterBlacklistClientesController
	} ]
};

SGRRotas.views.push(ManterFornecedorEscoltaView);
