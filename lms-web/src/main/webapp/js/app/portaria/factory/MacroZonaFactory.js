
(function(portariaModule){
	
	portariaModule.factory("MacroZonaFactory", [
		"$http", 
		 function($http) {
            	return {
            		findById: function(id) {
            			return $http.get(contextPath+"rest/portaria/macroZona/findById?id="+id)
            				.then(function(response){
            					return response.data;
            				});
            		},
            		findDispositivos: function(data) {
            			return $http.post(contextPath+"rest/portaria/macroZona/findDispositivos",data)
            				.then(function(response){
            					return response.data;
            				});
            		},
            		findVolumes: function(data) {
            			return $http.post(contextPath+"rest/portaria/macroZona/findVolumes",data)
            				.then(function(response){
            					return response.data;
            				});
            		}
            	};
            }
	]);
	
})(portariaModule);
