
(function(lmsDirectiveModule){
	
	lmsDirectiveModule.directive("lmsPedidoColeta", [ 
		"SuggestFactory", 
		function(SuggestFactory) {
			return SuggestFactory.createTemplate({
					suggest : "data as data.sgFilial + ' ' + (data.nrColeta | lpad: 6) + ' ' + (data.dhPedidoColeta | customDate) for data",
					inputLabel : "sgFilial",
					minLength : 4,
					url : contextPath + "rest/coleta/pedidoColeta/findPedidoColetaSuggest"
			});
		} 
	]);
	
})(lmsDirectiveModule);
