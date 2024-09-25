var ManterGruposEconomicosView =  { 
	name : "manterGruposEconomicos", 
	title : "manterGruposEconomicos", 
	controller : ManterGruposEconomicosController, 
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
		controller: ManterGruposEconomicosCadController
	}, {
 	   name: "cli",
	   title: "Clientes",
	   url: "/cli/:id",
	   disabled: "!$stateParams.id",
	   controller: ManterGruposEconomicosCliController
	} ] 
}; 
 
VendasRotas.views.push(ManterGruposEconomicosView); 
