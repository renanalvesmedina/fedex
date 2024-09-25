(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsTipoIdentificacaoCnpjCpf", [ 
		"ChosenFactory", 
		function(ChosenFactory) {
			return ChosenFactory.createTemplate({
					options : "data as data.dsIdentificador for data",
					track : "track by data.idIdentificador",
					url : contextPath + "rest/config/domainValue/findTipoIdentificacaoCnpjCpf"
			});
		} 
	]);
	
})(lmsDirectiveModule);
