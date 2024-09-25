
var ManterSolicitacaoEscoltaView = {
	name : "manterSolicitacaoEscolta",
	title : "manterSolicitacaoEscolta",
	controller : ManterSolicitacaoEscoltaController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab : true
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/:id",
		editTab : true,
		controller : ManterSolicitacaoEscoltaCadController
	}, {
		name : "hist",
		title : "historico",
		url : "/:id/historico",
		disabled : "!$stateParams.id",
		controller : ManterSolicitacaoEscoltaHistController
	} ]
};

SGRRotas.views.push(ManterSolicitacaoEscoltaView);
