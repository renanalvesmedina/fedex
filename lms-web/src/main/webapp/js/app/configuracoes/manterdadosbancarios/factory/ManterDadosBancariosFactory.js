
(function(configuracoesModule){

	configuracoesModule.factory("ManterDadosBancariosFactory", [
		"$http", 
		function($http) {
        	return {  
        		find: function(filter) {
        			return $http.post(contextPath+'rest/configuracoes/manterDadosBancarios/find', filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findById: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterDadosBancarios/findById?id=' + id)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		store: function(bean) {
        			return $http.post(contextPath+'rest/configuracoes/manterDadosBancarios/store', bean)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		removeDadosBancariosById: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterDadosBancarios/removeDadosBancariosById?id='+id)
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);
	
})(configuracoesModule);
