
(function(lmsDirectiveModule){

	lmsDirectiveModule.directive("lmsFilial", [
		"SuggestFactory",
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' - ' + data.nmFilial + ' - ' + data.nmEmpresa for data",
					inputLabel : "sgFilial",
					minLength : 3,
					url : contextPath + "rest/municipios/filialSuggest/findSuggest"
			});
		}
	]);

})(lmsDirectiveModule);
