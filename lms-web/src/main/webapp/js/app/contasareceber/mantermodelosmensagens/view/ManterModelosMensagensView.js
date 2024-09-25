
var ManterModelosMensagensView =  { 
	name : "manterModelosMensagens", 
	title : "manterModelosMensagens", 
	controller : ManterModelosMensagensController, 
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
	}, {
		name : "dad", 
		title : "Dados", 
		url : "/dados/:id",
		controller: ManterDadosModelosMensagensController,
	    disabled: "!$stateParams.id"
	} 
	] 
}; 
 
ContasAReceberRotas.views.push(ManterModelosMensagensView); 
