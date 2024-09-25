
var EmitirAlteracoesTarifasView =  { 
	name : "emitirAlteracoesTarifas", 
	title : "emitirRelatorioAlteracoesTarifa", 
	controller : EmitirAlteracoesTarifasController, 
	tabs : [ { 
		name : "pesq", 
		title : "relatorio", 
		url : "/" 
	} ] 
}; 

TabelaDePrecosRotas.views.push(EmitirAlteracoesTarifasView); 
