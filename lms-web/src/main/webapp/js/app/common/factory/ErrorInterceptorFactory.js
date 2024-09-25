
var ErrorInterceptorFactory = [
	"$rootScope",
	"$q",
	function($rootScope, $q) {
		return function(promise) {
			return promise.then(
				function(originalResponse) {
        			
        			$rootScope.servidorOffline = false;
        			$rootScope.redirectLogin = false;
        			
        			if (originalResponse.data) {
        				var key = (originalResponse.data.key && originalResponse.data.key.indexOf("LMS-") === 0) ? originalResponse.data.key : null;
        				if (originalResponse.data.error) {
        					var msg = originalResponse.data.error;
        					if (key) {
        						msg = originalResponse.data.key + ": " + msg;
        					}
        					$rootScope.addAlerts([{msg: msg, type: MESSAGE_SEVERITY.DANGER}]);
        					return $q.reject(originalResponse);
        				}
        				if (originalResponse.data.warning) {
        					var msg2 = originalResponse.data.warning;
        					if (key) {
        						msg2 = originalResponse.data.key + ": " + msg2;
        					}
        					$rootScope.addAlerts([{msg: msg2, type: MESSAGE_SEVERITY.WARNING}]);
        					return $q.reject(originalResponse);
        				}
        				if (originalResponse.data.info) {
        					var msg3 = originalResponse.data.info;
        					if (key) {
        						msg3 = originalResponse.data.key + ": " + msg3;
        					}
        					$rootScope.addAlerts([{msg: msg3, type: MESSAGE_SEVERITY.INFO}]);
        					return $q.reject(originalResponse);
        				}
        			}
        			
        			return originalResponse;
        			
        		}, 
        		function(originalResponse) {
        			
        			if (originalResponse.status != 401 && !$rootScope.redirectLogin) {

        				if(originalResponse.status === 0 || originalResponse.status == 12029) {
        					if (originalResponse.config && originalResponse.config.url && originalResponse.config.url.indexOf("/rest/") > -1) {
        						
        						if (!$rootScope.servidorOffline) {
        							if ($rootScope.popup) {
        								var msg = $rootScope.$eval("'servidorOffLine' | translate");
        								$rootScope.popup(msg);
        							} else {
        								console.log("O servidor nao esta respondendo, tente novamente em alguns minutos.");
        							}
        						}
        						
        						$rootScope.servidorOffline = true;
        						
        					}
        				} else {
        					if ($rootScope.addAlerts) {
        						$rootScope.addAlerts([{msg: "Ocorreu um erro no servidor.", type: MESSAGE_SEVERITY.DANGER}]);
        					} else {
        						console.log("Ocorreu um erro no servidor.");
        					}
        				}
        				
        			}
        			
        			return $q.reject(originalResponse);
        		}
    		);
		};
	} 
];
