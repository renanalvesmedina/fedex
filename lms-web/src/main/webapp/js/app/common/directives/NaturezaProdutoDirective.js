
(function(lmsDirectiveModule) {

	lmsDirectiveModule.directive("lmsNaturezaProduto", [
		"ChosenFactory",
		function(ChosenFactory) {
			return ChosenFactory.createTemplate({
					options : "data.id as data.dsNaturezaProduto for data",
					track : "",
					url : contextPath + "rest/expedicao/naturezaProduto/findChosen"
			});
		}
	]);

})(lmsDirectiveModule);
