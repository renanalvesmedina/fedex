
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsCotacao", [ 
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' ' + (data.nrCotacao | lpad: 6) + ' ' + data.dtGeracaoCotacao.dayOfMonth + '/' + data.dtGeracaoCotacao.monthOfYear + '/' + data.dtGeracaoCotacao.year for data",
					inputLabel : "sgFilial",
					minLength : 4,
					url : contextPath + "rest/vendas/cotacao/findCotacaoSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);

