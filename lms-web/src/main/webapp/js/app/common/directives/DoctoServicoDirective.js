
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsDoctoServico", [
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.tpDoctoServico.description + ' ' + data.dsDoctoServico + ' ' + (data.dhEmissao | customDate) for data",
					inputLabel : "dsDoctoServico",
					minLength : 4,
					url : contextPath + "rest/expedicao/doctoServico/findDoctoServicoSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);
