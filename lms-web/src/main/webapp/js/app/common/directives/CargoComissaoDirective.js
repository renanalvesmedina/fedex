
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsComissionado", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrMatricula + ' - ' + data.nmUsuario for data",
					inputLabel : "nrMatricula",
					minLength : 3,
					url : contextPath + "rest/vendas/cargoComissao/findComissionadoSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);



