
(function(lmsDirectiveModule){

	lmsDirectiveModule.directive("lmsRegional", [
		"SuggestFactory",
		function(SuggestFactory) {

			return SuggestFactory.createTemplate({
					suggest : "data as data.sgRegional +' '+ data.dsRegional for data",
					inputLabel : "sgRegional",
					minLength : 3,
					url : contextPath + "rest/municipios/regionalSuggest/findSuggest"
			});
		}
	]);

})(lmsDirectiveModule);

