
(function(lmsDirectiveModule) {

	lmsDirectiveModule.directive("lmsFornecedorEscolta", [
			"SuggestFactory", function(SuggestFactory) {
				return SuggestFactory.createTemplate({
					suggest : "data as data.nrIdentificacao + ' - ' + data.nmPessoa for data",
					inputLabel : "nrIdentificacao",
					minLength : 3,
					url : contextPath + "rest/sgr/fornecedorEscolta/findSuggest"
				});
			} ]);

})(lmsDirectiveModule);
