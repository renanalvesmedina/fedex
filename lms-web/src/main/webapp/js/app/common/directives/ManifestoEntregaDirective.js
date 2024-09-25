
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsManifestoEntrega", [
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' ' + (data.nrManifestoEntrega | lpad: 6) + ' ' + (data.dhEmissao | customDate) for data",
					inputLabel : "sgFilial",
					minLength : 4,
					url : contextPath + "rest/entrega/manifestoEntrega/findManifestoEntregaSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
