
(function(municipiosModule){

	municipiosModule.factory("EmpresaFactory", [
		"$http",
		function($http) {
			var empresasDoUsuarioCache;
			return {
				findByUsuarioLogado : function() {
					if (!empresasDoUsuarioCache) {
						var url = contextPath + "rest/municipios/empresa/findByUsuarioLogado";
						empresasDoUsuarioCache = $http.get(url)
									.then(function(response) {
										return response.data;
									});
					}
					return empresasDoUsuarioCache;
				}
			};
		}
	]);

})(municipiosModule);
