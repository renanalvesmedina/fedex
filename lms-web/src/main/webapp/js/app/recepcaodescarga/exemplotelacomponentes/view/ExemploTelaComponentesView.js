
var ExemploTelaComponentesView =  {
	name : "exemploTelaComponentes",
	title : "exemploTelaComponentes",
	controller : ExemploTelaComponentesController,
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
		name : "aba1",
		title : "SubAbas",
		url : "/aba1",
		controller: ExemploTelaComponentesAba1Controller
	}, {
		name : "file",
		title : "File",
		url : "/file",
		controller: ExemploTelaComponentesFileController
	}, {
		name : "legacy",
		title : "Legacy",
		url : "/legacy",
		controller: ExemploTelaComponentesLegacyController
	}, {
		name : "customTable",
		title : "Custom Table",
		url : "/customtable",
		controller: ExemploTelaComponentesCustomTableController
	} ]
};

RecepcaoDescargaRotas.views.push(ExemploTelaComponentesView);
