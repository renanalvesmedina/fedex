
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsPais", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgPais + ' - ' + data.nmPais + (data.cdIso ? ' - ' + data.cdIso : '') for data",
					inputLabel : "nmPais",
					minLength : 2,
					url : contextPath + "rest/municipios/paisSuggest/findSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
