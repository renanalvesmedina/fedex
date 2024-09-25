
(function(freteCarreteiroViagemModule){
	
	freteCarreteiroViagemModule.factory("ManterReciboComplementarFactory", [
		"$http", 
		function($http) {
        	return {
        		find: function(filter) {
        			return $http.post(contextPath+'rest/fretecarreteiroviagem/manterReciboComplementar/find', filter)
        				.then(function(response){
        					return response.data;
        				});
        		}, 
        		findById: function(id) {
        			return $http.get(contextPath+'rest/fretecarreteiroviagem/manterReciboComplementar/findById?id=' + id)
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);
	
})(freteCarreteiroViagemModule);

