
(function(configuracoesModule){

	configuracoesModule.factory("UsuarioLmsFactory", [
	    "$q",
		"$http",
		function($q, $http) {
			var usuario;
			return {
				findUsuarioLmsLogado : function() {
					var deferred = $q.defer();
					var url = contextPath + "rest/configuracoes/usuarioLms/findUsuarioLmsLogado";
					if (!usuario) {
						$http.get(url).then(
							function(response) {
								usuario = response.data;
								deferred.resolve(usuario);
							},
							function(error) {
								deferred.reject(error.data);
							}
						);
					} else {
						deferred.resolve(usuario);
					}
					return deferred.promise;
				}
			};
		}
	]);

})(configuracoesModule);

