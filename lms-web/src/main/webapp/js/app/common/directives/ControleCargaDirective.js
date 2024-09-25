
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsControleCarga", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' ' + (data.nrControleCarga | lpad: 6) + ' ' + (data.dhGeracao | customDate) for data",
					inputLabel : "sgFilial",
					minLength : 4,
					url : contextPath + "rest/carregamento/controleCargaSuggest/findSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
