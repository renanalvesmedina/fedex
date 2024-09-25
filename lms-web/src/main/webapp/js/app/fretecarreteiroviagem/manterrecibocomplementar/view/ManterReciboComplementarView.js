
var ManterReciboComplementarView = {
	name : "manterReciboComplementar",
	title : "manterRecibosComplementar",
	ignoreModuleName : true,
	controller : ManterReciboComplementarController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		disabled: "data.workflow",
		listTab: true,
		controller : ManterReciboComplementarListController
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		editTab: true,
		controller : ManterReciboComplementarCadController
	}, {
		name : "workflow",
		title : "historicoAprovacao",
		url : "/detalhe/:id/workflow",		
		disabled: "!$stateParams.id",
		controller : ManterReciboComplementarWorkflowController
	} ]
};

FreteCarreteiroViagemRotas.views.push(ManterReciboComplementarView);
