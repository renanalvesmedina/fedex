
var ConsultarLocalizacoesMercadoriasView = {
	name : "consultarLocalizacoesMercadorias",
	title : "consultarLocalizacoesMercadorias",
	url: "/consultarLocalizacaoMercadoria",
	ignoreModuleName : true,
	controller : ConsultarLocalizacoesMercadoriasController,
	tabs : [ {
		name : "consulta",
		title : "consulta",
		url : "/"
	}, {
		name : "listagem",
		title : "listagem",
		url : "/listagem",
		disabled: "listTableParams.list.length == 0"
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/cad/:id",
		disabled : "true",
		controller: ConsultarLocalizacoesMercadoriasCadController
	} ]
};

SimRotas.views.push(ConsultarLocalizacoesMercadoriasView);
