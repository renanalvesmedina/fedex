
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsUnidadeFederativa", [ 
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgUnidadeFederativa + ' - ' + data.nmUnidadeFederativa for data",
					inputLabel : "nmUnidadeFederativa",
					minLength : 1,
					url : contextPath + "rest/municipios/manterUnidadeFederativa/findUnidadeFederativaSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);

