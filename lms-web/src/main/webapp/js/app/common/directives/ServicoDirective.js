
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsServico", [ 
		"ChosenFactory", 
		function(ChosenFactory) {
			return ChosenFactory.createTemplate({
					options : "data as data.dsServico for data",
					track : "track by data.idServico",
					url : contextPath + "rest/configuracoes/servicoChosen/find"
			});
		} 
	]);
	
})(lmsDirectiveModule);
