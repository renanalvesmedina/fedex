
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsMeioTransporte", [
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrIdentificador + ' - ' + data.nrFrota for data",
					inputLabel : "nrIdentificador",
					minLength : 5,
					url : contextPath + "rest/contratacaoveiculos/meioTransporte/findMeioTransporteSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);

