
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsProprietario", [ 
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrIdentificacao + ' - ' + data.nmPessoa + ' - ' + data.tpProprietario for data",
					inputLabel : "nmPessoa",
					minLength : 4,
					url : contextPath + "rest/contratacaoveiculos/manterProprietario/findProprietarioSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);
