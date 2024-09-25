
(function(lmsCommonModule) {
	
	lmsCommonModule.factory("SystemInfoFactory", ["$rootScope", "$http", "$q", function($rootScope, $http, $q) {
		return {
		    atualizarInfo : function() {
		    	$rootScope.loadRecursos = false;
		    	var deferred = $q.defer();
		    	$http.get(contextPath+"rest/systemInfo") 
			   		.then(function(response) {
			   			$rootScope.chaveVersion = chaveVersion = response.data.chaveVersion;
			   			$rootScope.contextPath = contextPath = response.data.contextPath+"/";
			   			$rootScope.systemName = response.data.systemName;
			   			$rootScope.version = response.data.version;
			   			$rootScope.serverInfo = response.data.serverInfo;
			   		});
		    	return deferred.promise;
	        }
		};
	}]);
	
})(lmsCommonModule);
