
(function(configuracoesModule){
	
	configuracoesModule.factory("ManterTelefonePessoaFactory", [
		"$http", 
		function($http) {
        	return {  
        		find: function(filter) {
        			return $http.post(contextPath+'rest/configuracoes/manterTelefonePessoa/find', filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findById: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterTelefonePessoa/findById?id=' + id)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		store: function(bean) {
        			return $http.post(contextPath+'rest/configuracoes/manterTelefonePessoa/store', bean)
        				.then(function(response){
        					return response.data;
        				});
        		},         
        		removeTelefonePessoaById: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterTelefonePessoa/removeTelefonePessoaById?id='+id)
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);
	
})(configuracoesModule);	

