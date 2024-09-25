
(function(lmsDirectiveModule){

	lmsDirectiveModule.directive("lmsTabelaPreco", [ 
		"SuggestFactory", 
		"TabelaPrecoService", 
		function(SuggestFactory, TabelaPrecoService) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nomeTabelaAux for data",
					inputLabel : "nomeTabela",
					minLength : 1,
					url : contextPath +  "rest/tabeladeprecos/suggest"
			});
		} 
	]);

})(lmsDirectiveModule);
