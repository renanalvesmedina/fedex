var MonitoramentoCIOTView =  {
	name : "monitoramentoCIOT",
	title : "monitoramentoCIOT",
	controller : MonitoramentoCIOTController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab: true
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		disabled: "!$stateParams.id",
		editTab: true
	} ]
};

CarregamentoRotas.views.push(MonitoramentoCIOTView);