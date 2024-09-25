goog.require('app.global.viewDefinition.portaria'); 
goog.require('app.global.viewDefinition.portaria.InformarChegadaSaidaController'); 
 
goog.provide('app.global.viewDefinition.portaria.InformarChegadaSaidaView'); 
 
var InformarChegadaSaidaView =  { 
	name : "informarChegadaSaida", 
	title : "informarChegadaSaida", 
	controller : InformarChegadaSaidaController, 
	tabs : [ { 
		name : "portaria", 
		title : "portaria", 
		url : "/"
	}] 
}; 
 
PortariaRotas.views.push(InformarChegadaSaidaView); 
