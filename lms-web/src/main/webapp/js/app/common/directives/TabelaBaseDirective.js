
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsTabelaBase", [
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
				suggest: "data as data.tabelaPreco for data",
				inputLabel: "tabelaPreco",
				minLength: 1,
				url : contextPath + "rest/tabeladeprecos/reajustetabeladepreco/findTabelaBaseSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
