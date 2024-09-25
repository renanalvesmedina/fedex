
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsRotaColetaEntrega", [ 
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrRota + ' - ' + data.dsRota for data",
					inputLabel : "dsRota",
					minLength : 1,
					url : contextPath + "rest/municipios/manterRotaColetaEntrega/findRotaColetaEntregaSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);
