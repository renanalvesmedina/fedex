
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsMunicipio", [ 
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nmMunicipio for data",
					inputLabel : "nmMunicipio",
					minLength : 1,
					url : contextPath + "rest/municipios/manterMunicipio/findMunicipioSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);
