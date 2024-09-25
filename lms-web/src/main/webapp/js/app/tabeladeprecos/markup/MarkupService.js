
(function(tabelaDePrecosModule){

	tabelaDePrecosModule.service("MarkupService", [
		"$resource", 
		function($resource) {

            	var baseUrl = contextPath + "rest/tabeladeprecos/markup/";
            	var recurso;
            	var service = function() {
					if (!recurso) {
						recurso = $resource(baseUrl + ":id", {id: "@id"}, {
								storeAll: {
									method: "POST",
									url: baseUrl + "storeall",
									isArray: true
								},
								removeAll: {
									method: "POST",
									url: baseUrl + "removeall"
								}
							});
					}
					var retorno = {
						storeAll: function(markups, callbackSuccess, callbackError) {
							return recurso.storeAll(markups).$promise.then(callbackSuccess, callbackError);
						},
						removeAll: function(ids, callbackSuccess, callbackError) {
							return recurso.removeAll({}, ids).$promise.then(callbackSuccess, callbackError);
						}
					};
					retorno.prototype = recurso;
					return retorno;
				}();

				return service;
        }
	]);

})(tabelaDePrecosModule);

