var ManterClienteTerritorioTransferenciaCarteiraController = [
	"$rootScope",
	"$scope",
	"$stateParams",
	function($rootScope, $scope, $stateParams) {
		
		$scope.clear = function(){
			$scope.data = {};
		}

		$scope.transferir = function(){
			$rootScope.showLoading = true; 
			$scope.rest.doPost("transferirCarteira", $scope.data).then(function(response) {
				toastr.success("Sucesso");
			}, function() {
				$scope.data = {};
  				$rootScope.showLoading = false; 
			})['finally'](function(){
				$scope.data = {};
               	$rootScope.showLoading = false;
            });
		}

	}
];
