
var ManterExcecoesClientesView =  { 
	name : "manterExcecoesClientes", 
	title : "manterExcecoesClientes", 
	controller : ManterExcecoesClientesController, 
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
 
ContasAReceberRotas.views.push(ManterExcecoesClientesView); 
