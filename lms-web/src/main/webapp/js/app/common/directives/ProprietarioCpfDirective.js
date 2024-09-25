
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsProprietarioCpf", [ 
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrIdentificacao + ' - ' + data.nmPessoa + ' - ' + data.tpProprietario for data",
					inputLabel : "nmPessoa",
					minLength : 4,
					url : contextPath + "rest/contratacaoveiculos/manterProprietario/findProprietarioSuggestCpf"
			});
		} 
	]);
	
})(lmsDirectiveModule);
