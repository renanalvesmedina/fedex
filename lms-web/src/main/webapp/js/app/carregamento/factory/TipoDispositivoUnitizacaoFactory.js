(function(carregamentoModule){
	
	carregamentoModule.factory("TipoDispositivoUnitizacaoFactory", [
		"$http", 
		function($http) {
        	return {
        		findTipoDispositivoUnitizacao: function() {
        			return $http.get(contextPath+'rest/carregamento/tipoDispositivoUnitizacao/findTipoDispositivoUnitizacao')
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);
	
})(carregamentoModule);

