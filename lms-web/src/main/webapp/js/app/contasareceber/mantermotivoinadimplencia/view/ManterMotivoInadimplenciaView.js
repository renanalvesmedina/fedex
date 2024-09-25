
var ManterMotivoInadimplenciaView =  { 
	name : "manterMotivoInadimplencia", 
	title : "manterMotivoInadimplencia", 
	controller : ManterMotivoInadimplenciaController, 
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
 
ContasAReceberRotas.views.push(ManterMotivoInadimplenciaView); 
