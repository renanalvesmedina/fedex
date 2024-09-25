
(function(configuracoesModule){
	
	configuracoesModule.factory("ManterLoteCobrancaFactory", [
		"$http",
		function($http) {
        	return {  
        		storeItemLoteCobranca: function(bean) {
        			return $http.post(contextPath+'rest/contasareceber/loteCobranca/storeItemLoteCobranca', bean)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findItensLoteCobranca: function(id) {
	    			return $http.post(contextPath+'rest/contasareceber/loteCobranca/findItensLoteCobranca?id='+id.id)
	    				.then(function(response){
	    					return response.data;
	    				});
	    		},
	    		findItemLoteCobranca: function(id) {
	    			return $http.post(contextPath+'rest/contasareceber/loteCobranca/findItemLoteCobranca?id='+id)
	    				.then(function(response){
	    					return response.data;
	    				});
	    		}
        	};
        }
	]);
	
})(configuracoesModule);
