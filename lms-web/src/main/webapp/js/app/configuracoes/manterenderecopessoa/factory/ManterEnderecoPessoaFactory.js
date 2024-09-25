
(function(configuracoesModule){
	
	configuracoesModule.factory("ManterEnderecoPessoaFactory", [
		"$http",
		function($http) {
        	return {  
        		find: function(filter) {
        			return $http.post(contextPath+'rest/configuracoes/manterEnderecoPessoa/find', filter)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findById: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterEnderecoPessoa/findById?id=' + id)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		store: function(bean) {
        			return $http.post(contextPath+'rest/configuracoes/manterEnderecoPessoa/store', bean)
        				.then(function(response){
        					return response.data;
        				});
        		},         
        		findCep: function(cep) {
        			return $http.get(contextPath+'rest/configuracoes/manterEnderecoPessoa/findCep?cep=' + cep)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		populateTipoLogradouro: function(cep) {
        			return $http.get(contextPath+'rest/configuracoes/manterEnderecoPessoa/populateTipoLogradouro')
        				.then(function(response){
        					return response.data;
        				});
        		},
        		removeEnderecoPessoaById: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterEnderecoPessoa/removeEnderecoPessoaById?id='+id)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findEnderecoPessoaVigente: function(bean) {
        			return $http.post(contextPath+'rest/configuracoes/manterEnderecoPessoa/findEnderecoPessoaVigente',bean)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		loadTpEndereco: function(tpPessoa) {
        			return $http.post(contextPath+'rest/configuracoes/manterEnderecoPessoa/loadTpEndereco', tpPessoa)
        			.then(function(response){
        				return response.data;
        			});
        		},
        		createEnderecoPessoa: function(id) {
        			return $http.get(contextPath+'rest/configuracoes/manterEnderecoPessoa/createEnderecoPessoa?id='+id)
        				.then(function(response){
        					return response.data;
        				});
        		}
        	};
        }
	]);
	
})(configuracoesModule);
