
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsBanco", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrBanco + ' - ' + data.nmBanco for data",
					inputLabel : "nmBanco",
					minLength : 1,
					url : contextPath + "rest/configuracoes/bancoSuggest/findSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
