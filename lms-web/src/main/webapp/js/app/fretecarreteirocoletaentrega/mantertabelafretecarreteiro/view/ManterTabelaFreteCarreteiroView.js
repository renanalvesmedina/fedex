
var ManterTabelaFreteCarreteiroView =  {
	name : "manterTabelaFreteCarreteiro",
	title : "manterTabelasFretesPadrao",
	controller : ManterTabelaFreteCarreteiroController,
	tabs : [ {
		name : "list",
		title : "listagem",
		url : "/",
		listTab: true,
		controller : ManterTabelaFreteCarreteiroListController
	}, {
		name : "cad",
		title : "detalhamento",
		url : "/detalhe/:id",
		editTab: true,
		disabled: "!tabelafretecarreteiro.isMatriz && !$stateParams.id",
		controller : ManterTabelaFreteCarreteiroCadController
	}, {
		name : "anexos",
		title : "anexos",
		url : "/anexos/:id",
		disabled: "!tabelafretecarreteiro.isMatriz || !$stateParams.id",
		controller : ManterTabelaFreteCarreteiroAnexoController 
	},	
	{
		name : "rotas",
		title : "rotasColetaEntrega",
		url : "/rotas/:id",
		disabled : "!$stateParams.id",
		controller : ManterTabelaFreteCarreteiroTabController
	},
	{
		name : "proprietarios",
		title : "proprietarios",
		url : "/proprietarios/:id",
		disabled : "!$stateParams.id",
		controller : ManterTabelaFreteCarreteiroTabController
	},
	{
		name : "clientes",
		title : "clientes",
		url : "/clientes/:id",
		disabled : "!$stateParams.id",
		controller : ManterTabelaFreteCarreteiroTabController
	},	
	{
		name : "municipios",
		title : "municipios",
		url : "/municipios/:id",
		disabled : "!$stateParams.id",
		controller : ManterTabelaFreteCarreteiroTabController
	},
	{
		name : "tiposMeioTransporte",
		title : "tiposMeiosTransporte",
		url : "/tiposMeioTransporte/:id",
		disabled : "!$stateParams.id",
		controller : ManterTabelaFreteCarreteiroTabController
	},
	{
		name : "meiosTransporte",
		title : "meiosTransporte",
		url : "/meiosTransporte/:id",
		disabled : "!$stateParams.id",
		controller : ManterTabelaFreteCarreteiroTabController
	}]
};

FreteCarreteiroColetaEntregaRotas.views.push(ManterTabelaFreteCarreteiroView);
