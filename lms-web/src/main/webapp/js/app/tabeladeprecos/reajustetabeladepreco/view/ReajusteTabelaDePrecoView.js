
var ReajusteTabelaDePrecoView = {
	name : "reajustetabeladepreco",
	title : "reajustetabeladepreco",
	url : "/reajustetabeladepreco",
	service: "ReajusteTabelaDePrecoService",
	controller : ReajusteTabelaDePrecoController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		disabled : false,
		listTab: true,
		controller: ReajusteTabelaDePrecoListController
	}, {
		name : "detail",
		title : "detalhamento",
		url : "/detail/:id",
		disabled : false,
		controller: ReajusteTabelaDePrecoDetailController,
		editTab: true
	} , {
		name : "reajuste",
		title : "reajuste",
		url : "/reajuste/:id",
		disabled : "!$stateParams.id",
		controller : ReajusteTabelaDePrecoAbaReajusteController	}]
};

TabelaDePrecosRotas.views.push(ReajusteTabelaDePrecoView);
