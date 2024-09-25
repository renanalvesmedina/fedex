
(function(municipiosModule){
	
	municipiosModule.factory("ManterPaisFactory", [
		"$http", 
		function($http) {
        	return {  
        		findPaisUsuarioLogado: function(value, filter) {            			
        			return $http.get(contextPath+'rest/municipios/manterPais/findPaisUsuarioLogado').then(function(response){
        				return response.data;
        			});
        		}
        	};
        }
	]);
	
})(municipiosModule);

