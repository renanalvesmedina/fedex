
(function(configuracoesModule){
	
	configuracoesModule.factory("ManterDadosModeloMensagemFactory", [
		"$http",
		function($http) {
        	return {  
        		storeDadosModeloMensagem: function(bean) {
        			return $http.post(contextPath+'rest/contasareceber/modelosMensagens/storeDadosModeloMensagem', bean)
        				.then(function(response){
        					return response.data;
        				});
        		},
        		findDadosModeloMensagem: function(id) {
	    			return $http.post(contextPath+'rest/contasareceber/modelosMensagens/findDadosModeloMensagem?id='+id.id)
	    				.then(function(response){
	    					return response.data;
	    				});
	    		},
	    		findDadoModeloMensagem: function(id) {
	    			return $http.post(contextPath+'rest/contasareceber/modelosMensagens/findDadoModeloMensagem?id='+id)
	    				.then(function(response){
	    					return response.data;
	    				});
	    		}
        	};
        }
	]);
	
})(configuracoesModule);
