
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsMunicipioFilial", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nmMunicipio  + ' - ' + data.sgFilial for data", 
					inputLabel : "nmMunicipio", 
					minLength : 1, 
					url : contextPath + "rest/municipios/municipioFilialSuggest/findSuggest"
			});
		}]);
	
})(lmsDirectiveModule);