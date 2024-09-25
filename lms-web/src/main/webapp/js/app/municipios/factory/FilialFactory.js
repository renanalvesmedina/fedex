
(function(municipiosModule){
	
	municipiosModule.factory("FilialFactory", [
		"$http", 
		function($http) {
			return {
				findFilialUsuarioLogado : function() {
					var url = contextPath + "rest/municipios/filial/findFilialUsuarioLogado";
					return $http.get(url)
							.then(function(response) {
								return response.data;
							});
				}
			};
		} 
	]);
	
})(municipiosModule);
