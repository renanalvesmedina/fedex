
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsCliente", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrIdentificacao + ' - ' + data.nmPessoa + (data.nmMunicipio ? (' - ' + data.nmMunicipio + '-' + data.sgUnidadeFederativa) : '') for data",
					inputLabel : "nrIdentificacao",
					minLength : 1,
					url : contextPath + "rest/vendas/clienteSuggest/findSuggest"
			});
		}]);
	
})(lmsDirectiveModule);
