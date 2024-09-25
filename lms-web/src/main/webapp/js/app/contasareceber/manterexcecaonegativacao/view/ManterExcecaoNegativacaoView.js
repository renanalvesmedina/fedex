
var ManterExcecaoNegativacaoView =  { 
	name : "manterExcecaoNegativacao", 
	title : "manterExcecaoNegativacao", 
	controller : ManterExcecaoNegativacaoController, 
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
 
ContasAReceberRotas.views.push(ManterExcecaoNegativacaoView); 
