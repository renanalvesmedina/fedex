
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsRecibo", [ 
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' ' + (data.nrReciboFreteCarreteiro | lpad: 10) + ' - ' + data.tpReciboFreteCarreteiro for data",
					inputLabel : "nrReciboFreteCarreteiro",
					minLength : 1,
					url : contextPath + "rest/fretecarreteirocoletaentrega/manterRecibo/findReciboSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);
