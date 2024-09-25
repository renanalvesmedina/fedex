
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsCiaAerea", [
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrIdentificacao + ' ' + data.nmPessoa for data",
					inputLabel : "nmPessoa",
					minLength : 3,
					url : contextPath + "rest/municipios/ciaAereaSuggest/findSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);

