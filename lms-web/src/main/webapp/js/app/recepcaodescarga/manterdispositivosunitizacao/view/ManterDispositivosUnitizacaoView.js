
var ManterDispositivosUnitizacaoView =  {
	name : "manterDispositivosUnitizacao",
	title : "manterDispositivosUnitizacao",
	ignoreModuleName : true,
	controller : ManterDispositivosUnitizacaoController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab: true
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		editTab: true
	}, {
		name : "conteudo",
		title : "conteudo",
		url : "/detalhe/:id/conteudo",
		disabled : "!$stateParams.id",
		controller : ManterDispositivosUnitizacaoConteudoController
	}, {
		name : "eventos",
		title : "eventos",
		url : "/detalhe/:id/eventos",
		disabled : "!$stateParams.id",
		controller : ManterDispositivosUnitizacaoEventosController
	} ]
};

RecepcaoDescargaRotas.views.push(ManterDispositivosUnitizacaoView);
