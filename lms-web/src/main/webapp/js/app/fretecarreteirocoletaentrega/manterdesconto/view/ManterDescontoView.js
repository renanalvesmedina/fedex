
var ManterDescontoView =  {
	name : "manterDesconto",
	title : "manterDescontos",
	ignoreModuleName : true,
	controller : ManterDescontoController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		disabled: "data.workflow",
		listTab: true,
		controller : ManterDescontoListController
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		editTab: true,
		controller : ManterDescontoCadController
	},
	{
		name : "workflow",
		title : "historicoAprovacao",
		url : "/detalhe/:id/workflow",					
		disabled : "!$stateParams.id",
		controller : ManterDescontoWorkflowController
	}]
};

FreteCarreteiroColetaEntregaRotas.views.push(ManterDescontoView);
