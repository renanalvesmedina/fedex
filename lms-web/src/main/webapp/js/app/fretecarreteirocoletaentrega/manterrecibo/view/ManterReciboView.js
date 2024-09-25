
var ManterReciboView = {
	name : "manterRecibo",
	title : "manterPreFatura",
	ignoreModuleName : true,
	controller : ManterReciboController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		disabled: "dados.workflow",
		controller : ManterReciboListController
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		disabled: "!$stateParams.id",
		controller : ManterReciboCadController
	},
	{
		name : "notas",
		title : "notasCredito",
		url : "/detalhe/:id/notas",
		disabled: "!$stateParams.id"		
	},	
	{
		name : "complementar",
		title : "recibosComplementares",
		url : "/detalhe/:id/complementar",
		disabled: "!$stateParams.id",
		controller : ManterReciboComplementarController
	},				
	{
		name : "workflow",
		title : "historicoAprovacao",
		url : "/detalhe/:id/workflow",
		disabled : "!$stateParams.id",
		controller : ManterReciboWorkflowController
	} ]
};

FreteCarreteiroColetaEntregaRotas.views.push(ManterReciboView);
