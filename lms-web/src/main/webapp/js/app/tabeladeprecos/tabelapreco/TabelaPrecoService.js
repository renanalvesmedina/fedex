
(function(tabelaDePrecosModule){
	
	tabelaDePrecosModule.service("TabelaPrecoService", [
		"$resource", 
		function($resource) {

					var baseUrl = contextPath + "rest/tabeladeprecos/";

					var service = function() {
						if (!recurso) {
							var recurso = $resource(baseUrl + ":id", {id: "@id"}, {
									getParcelas: {
										method: "GET",
										params: {id: "@id"},
										url: baseUrl + ":id/parcelas",
										isArray: true
									}
								});
						}
						var retorno = {
							getParcelas: function(value) {
								return recurso.getParcelas({id: value}).$promise.then(function(data) {
									return data;
								});
							}
						};
						retorno.prototype = recurso;
						return retorno;
					}();

					return service;

		}
	]);

})(tabelaDePrecosModule);

