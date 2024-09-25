
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsManifestoColeta", [ 
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' ' + (data.nrManifesto | lpad: 6) + ' ' + (data.dhGeracao | customDate) for data",
					inputLabel : "sgFilial",
					minLength : 4,
					url : contextPath + "rest/coleta/manifestoColeta/findManifestoColetaSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);
