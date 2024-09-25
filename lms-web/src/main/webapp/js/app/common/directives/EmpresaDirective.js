
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsEmpresa", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrIdentificacao + ' ' + data.nmPessoa for data",
					inputLabel : "nmPessoa",
					minLength : 3,
					url : contextPath + "rest/municipios/empresaSuggest/findSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
