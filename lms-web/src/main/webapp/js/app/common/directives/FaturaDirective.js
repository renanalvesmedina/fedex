
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsFatura", [
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.nrFatura for data",
					inputLabel : "nrFatura",
					minLength : 1,
					url : contextPath + "rest/contasareceber/excecaoNegativacao/findFilialSuggest"
			});
		}]);
	
})(lmsDirectiveModule);

