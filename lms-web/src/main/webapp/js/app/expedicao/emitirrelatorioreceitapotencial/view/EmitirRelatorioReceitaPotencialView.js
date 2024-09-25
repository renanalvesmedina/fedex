
var EmitirRelatorioReceitaPotencialView =  { 
	name : "emitirRelatorioReceitaPotencial", 
	title : "emitirRelatorioReceitaPotencial", 
	controller : EmitirRelatorioReceitaPotencialController, 
	tabs : [ { 
		name : "list", 
		title : "filtros", 
		url : "/", 
		listTab: true 
	} ] 
}; 
 
ExpedicaoRotas.views.push(EmitirRelatorioReceitaPotencialView); 
