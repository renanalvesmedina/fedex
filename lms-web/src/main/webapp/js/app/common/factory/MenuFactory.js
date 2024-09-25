
(function(menuModule) {
	menuModule.factory("MenuFactory", ["$http", function($http) {
		return {
		    menu : function() {
		    	return $http.get(contextPath+'rest/menu/getMenuUsuario');
	        }
		};
	}]);
	
})(menuModule);

