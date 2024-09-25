
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsManifestoViagemNacional", [
	    "SuggestFactory", 
	    function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' ' + data.nrManifestoOrigem for data",
					inputLabel : "sgFilial",
					minLength : 4,
					url : contextPath + "rest/expedicao/manifestoViagemNacional/findManifestoViagemNacionalSuggest"
			});
		}
    ]);
	
})(lmsDirectiveModule);
