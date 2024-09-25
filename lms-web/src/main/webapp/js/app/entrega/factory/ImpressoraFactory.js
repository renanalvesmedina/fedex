(function(entregaModule){
	
	entregaModule.factory("ImpressoraFactory", [
		"$http", 
		function($http) {
        	return {
        		findImpressora: function() {
        			return $http.get(contextPath+'rest/entrega/impressora/findImpressora')
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);
	
})(entregaModule);

