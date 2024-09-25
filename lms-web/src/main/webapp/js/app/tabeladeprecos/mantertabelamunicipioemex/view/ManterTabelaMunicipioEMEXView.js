
var ManterTabelaMunicipioEMEXView =  {
	name : "manterTabelaMunicipioEMEX",
	title : "manterTabelaMunicipioEMEX",
	controller : ManterTabelaMunicipioEMEXController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab: true
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		editTab: true,
		controller: ManterTabelaMunicipioEMEXControllerCad
	} ]
};

TabelaDePrecosRotas.views.push(ManterTabelaMunicipioEMEXView);