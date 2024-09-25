
(function(lmsDirectiveModule) {

	lmsDirectiveModule.directive("lmsMotorista", [
			"SuggestFactory", function(SuggestFactory) {
				return SuggestFactory.createTemplate({
					suggest : "data as data.nrIdentificacao + ' - ' + data.nmPessoa for data",
					inputLabel : "nrIdentificacao",
					minLength : 3,
					url : contextPath + "rest/contratacaoveiculos/motoristaSuggest/findSuggest"
				});
			} ]);

})(lmsDirectiveModule);
