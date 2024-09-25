
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsAgenciaBancaria", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrAgenciaBancaria + ' - ' + data.nmAgenciaBancaria for data",
					inputLabel : "nmAgenciaBancaria",
					minLength : 1,
					url : contextPath + "rest/configuracoes/agenciaBancariaSuggest/findSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
