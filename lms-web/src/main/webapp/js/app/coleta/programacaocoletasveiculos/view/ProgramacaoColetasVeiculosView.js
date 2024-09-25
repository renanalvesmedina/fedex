var ProgramacaoColetasVeiculosView = {
	name : "programacaoColetasVeiculos",
	title : "programacaoColetasVeiculos",
	controller : ProgramacaoColetasVeiculosController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab : true	
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		disabled: "!$stateParams.id",
		editTab: true,
		controller : ProgramacaoColetasVeiculosControllerCadController
	} ]
};

ColetaRotas.views.push(ProgramacaoColetasVeiculosView);
