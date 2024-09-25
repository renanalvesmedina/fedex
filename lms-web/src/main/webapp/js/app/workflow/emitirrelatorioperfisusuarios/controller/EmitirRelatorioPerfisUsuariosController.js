var EmitirRelatorioPerfisUsuariosController = [
	"$rootScope", 
	"$scope", 
	function($rootScope, $scope) {
		
		$scope.setConstructor({
			rest: "/workflow/emitirRelatorioPerfisUsuarios"
		});

		$scope.find = function() {
			$rootScope.showLoading = true;
			
			$scope.rest.doPost("find", $scope.filter).then(function(data) {
				$rootScope.showLoading = false;
				if (data.reportLocator) {
					location.href = contextPath + "/viewBatchReport?" + data.reportLocator;
				}
			}, function() {
				$rootScope.showLoading = false;
			});
		};
		
		$scope.limpar = function() {
			$scope.filter = {};
			$scope.initializeAbaPesq();
		};
	}
];
