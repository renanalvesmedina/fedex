
(function(contratacaoVeiculosModule){

	contratacaoVeiculosModule.factory("ManterBloqueiosMotoristaProprietarioFactory", [
		"$http", 
		function($http) {
        	return {  
        		store: function(data) {
        			return $http.post(contextPath+'rest/contratacaoveiculos/manterBloqueiosMotoristaProprietario/store', data)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findBloqueioByProprietario: function(id) {
        			return $http.get(contextPath+'rest/contratacaoveiculos/manterBloqueiosMotoristaProprietario/findBloqueioByProprietario?id='+id)
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);

})(contratacaoVeiculosModule);
