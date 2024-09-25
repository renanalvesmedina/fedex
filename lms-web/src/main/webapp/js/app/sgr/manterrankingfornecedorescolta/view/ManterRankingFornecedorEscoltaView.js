
var ManterRankingFornecedorEscoltaView =  { 
	name : "manterRankingFornecedorEscolta", 
	title : "manterRankingFornecedorEscolta", 
	controller : ManterRankingFornecedorEscoltaController, 
	tabs : [ { 
		name : "list", 
		title : "listagem", 
		url : "/", 
		listTab: true 
	}, { 
		name : "cad", 
		title : "detalhamento", 
		url : "/:id", 
		editTab: true 
	} ] 
}; 
 
SGRRotas.views.push(ManterRankingFornecedorEscoltaView); 
