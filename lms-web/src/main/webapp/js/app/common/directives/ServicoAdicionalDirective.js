
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsServicoAdicional", [ 
		"ChosenFactory", 
		function(ChosenFactory) {
			return ChosenFactory.createTemplate({
					options : "data as data.dsServicoAdicional for data",
					track : "track by data.idServicoAdicional",
					url : contextPath + "rest/configuracoes/servicoAdicionalChosen/find"
			});
		} 
	]);
	
})(lmsDirectiveModule);
