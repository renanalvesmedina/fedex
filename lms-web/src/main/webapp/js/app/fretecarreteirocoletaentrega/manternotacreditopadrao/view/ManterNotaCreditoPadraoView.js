
var ManterNotaCreditoPadraoView =  {
	name : "manterNotaCreditoPadrao",
	title : "manterNotasCreditoPadrao",
	controller : ManterNotaCreditoPadraoController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		disabled: "data.workflow",
		listTab: true,
		controller : ManterNotaCreditoPadraoListController
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		editTab: true,
		disabled : "!$stateParams.id",
		controller : ManterNotaCreditoPadraoCadController
	},
	{
		name : "workflow",
		title : "historicoAprovacao",
		url : "/detalhe/:id/workflow",					
		disabled : "!$stateParams.id",
		controller : ManterNotaCreditoPadraoWorkflowController
	}]
};

FreteCarreteiroColetaEntregaRotas.views.push(ManterNotaCreditoPadraoView);
