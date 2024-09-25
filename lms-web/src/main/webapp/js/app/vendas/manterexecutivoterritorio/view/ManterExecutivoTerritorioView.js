var ManterExecutivoTerritorioView =  { 
	name : "manterExecutivoTerritorio", 
	title : "manterExecutivoTerritorio", 
	controller : ManterExecutivoTerritorioController, 
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
	} ] 
}; 
 
VendasRotas.views.push(ManterExecutivoTerritorioView); 
