
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsFuncionarioPromotor", [ 
		"SuggestFactory", 
		function(SuggestFactory) {

			return SuggestFactory.createTemplate({
					suggest : "data as data.nmUsuario for data",
					inputLabel : "nrMatricula",
					minLength : 3,
					url : contextPath + "rest/vendas/promotor/findFuncionarioPromotorSuggest"
			});

		} 
	]);
	
})(lmsDirectiveModule);

