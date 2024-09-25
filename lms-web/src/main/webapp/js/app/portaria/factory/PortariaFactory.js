
(function(portariaModule){
	
	portariaModule.service("PortariaFactory", [
		"$http", 
		function($http) {
        	return {  
        		portariasFilial: function(filter) {
        			return $http.post(contextPath+'rest/portaria/filial')
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);
	
})(portariaModule);
