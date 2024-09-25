
var MonitoramentoNetworkAereoView =  { 
	name : "monitoramentoNetworkAereo", 
	title : "monitoramentoNetworkAereo", 
	controller : MonitoramentoNetworkAereoController, 
	tabs : [ { 
		name : "list", 
		title : "listagem", 
		url : "/" 
	}, { 
		name : "listCiaAerea", 
		title : "acompanhamento", 
		url : "/acompanhamento", 
		disabled: "!controleAbas.exibeAcompanhamento",
		controller: MonitoramentoNetworkAereoListCiaAereaController
	}, { 
		name : "awbsList", 
		title : "detalhamento", 
		url : "/acompanhamentoAwb",
		disabled: "!controleAbas.exibeAcompanhamentoAwb",
		controller: MonitoramentoNetworkAereoAwbsListController
	} ] 
}; 
 
ExpedicaoRotas.views.push(MonitoramentoNetworkAereoView); 
