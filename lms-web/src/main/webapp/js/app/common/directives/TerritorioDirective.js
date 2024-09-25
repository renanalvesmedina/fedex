(function(lmsDirectiveModule){

	lmsDirectiveModule.directive("lmsTerritorio", [
		"SuggestFactory",
		function(SuggestFactory) {

			return SuggestFactory.createTemplate({
				suggest : "data as data.nmTerritorio for data",
				inputLabel : "nmTerritorio",
				minLength : 3,
				url : contextPath + "rest/vendas/territorioSuggest/findSuggest"
			});

		}
	]);

})(lmsDirectiveModule);
