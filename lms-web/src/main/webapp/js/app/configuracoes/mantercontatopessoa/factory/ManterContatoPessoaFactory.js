
(function(configuracoesModule){
	
	configuracoesModule.factory("ManterContatoPessoaFactory", [
		"$http", 
		function($http) {
        	return {  
        		find: function(filter) {
        			return $http.post(contextPath+'rest/configuracoes/manterContatoPessoa/find', filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findById: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterContatoPessoa/findById?id=' + id)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		store: function(bean) {
        			return $http.post(contextPath+'rest/configuracoes/manterContatoPessoa/store', bean)
        				.then(function(response){
        					return response.data;
        				});
        		},         
        		removeContatoPessoaById: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterContatoPessoa/removeContatoPessoaById?id='+id)
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);
	
})(configuracoesModule);
