
var ManterLoteCobrancaView =  { 
	name : "manterLoteCobranca", 
	title : "manterLoteCobranca", 
	controller : ManterLoteCobrancaController, 
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
		controller: ManterLoteCobrancaCadController
	} , { 
		name : "fatura", 
		title : "fatura", 
		url : "/fatura/:id",
		controller: ManterLoteCobrancaFaturaController,
		disabled : "!$stateParams.id"
	}
	] 
}; 
 
ContasAReceberRotas.views.push(ManterLoteCobrancaView); 
