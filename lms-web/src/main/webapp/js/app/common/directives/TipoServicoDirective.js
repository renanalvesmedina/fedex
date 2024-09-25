
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsTipoServico", [ 
		"ChosenFactory", 
		function(ChosenFactory) {
			return ChosenFactory.createTemplate({
					options : "data as data.dsTipoServico for data",
					track : "track by data.idTipoServico",
					url : contextPath + "rest/sim/consultarLocalizacaoMercadoria/findTipoServico"
			});
		} 
	]);
	
})(lmsDirectiveModule);

