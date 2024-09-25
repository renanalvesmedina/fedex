
var ConsultarRetornoDeBancoView =  { 
	name : "consultarRetornoDeBanco", 
	title : "consultarRetornoDeBanco", 
	controller : ConsultarRetornoDeBancoController, 
	tabs : [ { 
		name : "list", 
		title : "listagem", 
		url : "/", 
		listTab: true 
	} ] 
}; 
 
ContasAReceberRotas.views.push(ConsultarRetornoDeBancoView); 
