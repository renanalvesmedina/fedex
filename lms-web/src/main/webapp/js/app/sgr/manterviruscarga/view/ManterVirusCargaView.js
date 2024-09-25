
var ManterVirusCargaView =  { 
	name : "manterVirusCarga", 
	title : "manterVirusCarga", 
	controller : ManterVirusCargaController, 
	tabs : [ {  
		name : "list", 
		title : "listagem", 
		url : "/", 
		listTab: true 
	}, { 
		name : "cad", 
		title : "detalhamento", 
		url : "/:id", 
		editTab: true ,
		controller: ManterVirusCargaCadController
	} ] 
}; 
 
SGRRotas.views.push(ManterVirusCargaView); 