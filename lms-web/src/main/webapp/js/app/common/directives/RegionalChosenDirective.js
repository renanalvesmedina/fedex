
(function(lmsDirectiveModule){

	lmsDirectiveModule.directive("lmsRegionalChosen", [
		"ChosenFactory",
		function(ChosenFactory) {
			return ChosenFactory.createTemplate({
					options : "data as data.dsRegional for data",
					track : "track by data.idRegional",
					url : contextPath + "rest/municipios/regionalChosen/findChosen"
			});
		}
	]);

})(lmsDirectiveModule);

