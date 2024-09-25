(function(vendasModule){
	
	vendasModule.service("ClienteService", [
		"$resource",
		function($resource) {

        	var baseUrl = contextPath + 'rest/vendas/cliente/';
        	var recurso;

        	return function(){
        		if (recurso) {
        			return recurso;
        		}
        		recurso = $resource(baseUrl + ':id', {id: '@id'}, {
        			buscaDivisoesCliente: {
        				method: 'GET',
        				url: baseUrl + ":id/divisoes",
        				isArray: true
        			}
        		});
        		var retorno = {
        			buscaDivisoesCliente: function(idCliente) {
        				return recurso.buscaDivisoesCliente({id: idCliente}).$promise.then(function (divisoes) {
        					return divisoes;
        				});
        			}
        		};
        		return retorno;
        	}();
        }
	]);
	
})(vendasModule);

