
var ManterAliquotaFundoCombatePobrezaView =  {
	name : "manterAliquotaFundoCombatePobreza",
	title : "manterAliquotaFundoCombatePobreza",
	controller : ManterAliquotaFundoCombatePobrezaController,
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
		controller: ManterAliquotaFundoCombatePobrezaControllerCad
	} ]
};

TributosRotas.views.push(ManterAliquotaFundoCombatePobrezaView);