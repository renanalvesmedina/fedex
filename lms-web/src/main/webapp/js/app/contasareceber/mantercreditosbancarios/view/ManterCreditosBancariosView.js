
var ManterCreditosBancariosView =  { 
	name : "manterCreditosBancarios", 
	title : "manterCreditosBancarios", 
	controller : ManterCreditosBancariosController, 
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
		controller: ManterCreditosBancariosCadController
	} ] 
}; 
 
ContasAReceberRotas.views.push(ManterCreditosBancariosView); 
