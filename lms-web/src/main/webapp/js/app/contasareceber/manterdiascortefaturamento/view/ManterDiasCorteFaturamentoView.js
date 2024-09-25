
var ManterDiasCorteFaturamentoView =  { 
	name : "manterDiasCorteFaturamento", 
	title : "manterDiasCorteFaturamento", 
	controller : ManterDiasCorteFaturamentoController, 
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
 
ContasAReceberRotas.views.push(ManterDiasCorteFaturamentoView); 
