
(function(lmsDirectiveModule){

	lmsDirectiveModule.directive("lmsAeroporto", [
		"SuggestFactory",
		function(SuggestFactory) {

			return SuggestFactory.createTemplate({
					suggest : "data as data.sgAeroporto +' '+ data.nmAeroporto for data",
					inputLabel : "sgAeroporto",
					minLength : 3,
					url : contextPath + "rest/municipios/aeroportoSuggest/findSuggest"
			});
		}
	]);

})(lmsDirectiveModule);

