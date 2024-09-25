
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsRegiaoColetaEntrega", [ 
		"ChosenFactory", 
		function(ChosenFactory) {
			return ChosenFactory.createTemplate({
					options : "data as data.dsRegiaoColetaEntregaFil for data",
					track : "track by data.idRegiaoColetaEntregaFil",
					url : contextPath + "rest/configuracoes/regiaoColetaEntregaChosen/find"
			});
		} 
	]);
	
})(lmsDirectiveModule);
