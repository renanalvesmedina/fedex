
var SimularPlanoGerenciamentoRiscoView = {
	name : "simularPlanoGerenciamentoRisco",
	title : "simularPlanoGerenciamentoRisco",
	controller : SimularPlanoGerenciamentoRiscoController,
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
		disabled : "!$stateParams.id"
	} ]
};

SGRRotas.views.push(SimularPlanoGerenciamentoRiscoView);
