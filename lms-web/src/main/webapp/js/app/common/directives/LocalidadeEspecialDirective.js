
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsLocalidadeEspecial", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.dsLocalidade for data", 
					inputLabel : "dsLocalidade", 
					minLength : 1, 
					url : contextPath + "rest/municipios/localidadeEspecialSuggest/findSuggest"
			});
		}]);
	
})(lmsDirectiveModule);