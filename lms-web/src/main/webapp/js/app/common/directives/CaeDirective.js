
(function(lmsDirectiveModule){

	lmsDirectiveModule.directive("lmsCae", [
		"SuggestFactory",
		function(SuggestFactory) {

			return SuggestFactory.createTemplate({
				suggest : "data as data.nmFilial + ' - ' + data.nrCae for data",
				inputLabel : "nrCae",
				minLength : 3,
				url : contextPath + "rest/expedicao/caeSuggest/findSuggest"
			});

		}
	]);

})(lmsDirectiveModule);
