
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsAwb", [ 
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.dsFormatedAwb + ' ' + (data.dhEmissao | customDate) for data",
					inputLabel : "dsAwb",
					minLength : 4,
					url : contextPath + "rest/expedicao/awbSuggest/findSuggest"
			});
		}
	]);
	
})(lmsDirectiveModule);

