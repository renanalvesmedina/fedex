
var ManterLocalizacaoAwbCiaAereaView = {
	name : "manterLocalizacaoAwbCiaAerea",
	title : "manterLocalizacaoAwbCiaAerea",
	service: "LocalizacaoAwbCiaAereaService",
	controller : ManterLocalizacaoAwbCiaAereaController,
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
	}]
};

ExpedicaoRotas.views.push(ManterLocalizacaoAwbCiaAereaView);
	
